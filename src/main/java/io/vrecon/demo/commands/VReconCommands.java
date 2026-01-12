package io.vrecon.demo.commands;

import io.vrecon.demo.client.VReconApiClient;
import io.vrecon.demo.model.RecognizeResponse;
import io.vrecon.demo.model.StateResponse;

import java.io.IOException;

/**
 * Command handlers for VRecon CLI operations.
 */
public class VReconCommands {

    private final VReconApiClient client;

    public VReconCommands(String baseUrl, String apiKey, boolean verbose) {
        this.client = new VReconApiClient(baseUrl, apiKey, verbose);
    }

    /**
     * Execute the recognize command - submit an image for vehicle recognition.
     *
     * @param imagePath Path to the image file
     */
    public void recognize(String imagePath) {
        System.out.println("Submitting image for recognition: " + imagePath);
        System.out.println();

        try {
            RecognizeResponse response = client.recognize(imagePath);

            if (response.isSuccess()) {
                System.out.println("Recognition request submitted successfully!");
                System.out.println("Request UUID: " + response.getRequestUuid());
                System.out.println("Initial State: " + response.getState());
                System.out.println();
                System.out.println("Use the 'state' command to check the recognition status:");
                System.out.println("  java -jar vrecon-demo-client.jar state --uuid " + response.getRequestUuid());
            } else {
                System.err.println("Recognition request failed!");
                System.err.println("Error: " + response.getError());
            }
        } catch (IOException e) {
            System.err.println("Error submitting recognition request: " + e.getMessage());
        }
    }

    /**
     * Execute the state command - get the status of a recognition request.
     *
     * @param requestUuid The UUID of the recognition request
     * @param outputJson If true, output raw JSON response instead of formatted output
     */
    public void getState(String requestUuid, boolean outputJson) {
        if (!outputJson) {
            System.out.println("Checking recognition state for UUID: " + requestUuid);
            System.out.println();
        }

        try {
            String jsonResponse = client.getStateAsJson(requestUuid);

            if (outputJson) {
                System.out.println(jsonResponse);
                return;
            }

            StateResponse response = client.parseStateResponse(jsonResponse);

            if (response.isSuccess()) {
                System.out.println("Request UUID: " + response.getRequestUuid());
                System.out.println("State: " + response.getState());
                System.out.println("Created At: " + response.getCreatedAt());
                System.out.println("Updated At: " + response.getUpdatedAt());

                String state = response.getState();
                if (("DONE".equals(state) || "DELIVERED".equals(state)) && response.getResult() != null) {
                    System.out.println();
                    System.out.println("Recognition Result:");
                    System.out.println("-------------------");
                    printResult(response);
                } else if ("FAILED".equals(state)) {
                    System.out.println();
                    System.err.println("Recognition failed: " + response.getError());
                } else if ("PENDING".equals(state) || "PROCESSING".equals(state)) {
                    System.out.println();
                    System.out.println("Recognition is still in progress. Please check again later.");
                }
            } else {
                System.err.println("Failed to get recognition state!");
                System.err.println("Error: " + response.getError());
            }
        } catch (IOException e) {
            System.err.println("Error getting recognition state: " + e.getMessage());
        }
    }

    /**
     * Execute the recognize-and-wait command - submit and poll until complete.
     *
     * @param imagePath    Path to the image file
     * @param pollInterval Interval between polls in seconds
     * @param maxWaitTime  Maximum time to wait in seconds
     */
    public void recognizeAndWait(String imagePath, int pollInterval, int maxWaitTime) {
        System.out.println("Submitting image for recognition: " + imagePath);
        System.out.println("Polling interval: " + pollInterval + " seconds");
        System.out.println("Max wait time: " + maxWaitTime + " seconds");
        System.out.println();

        try {
            System.out.println("Waiting for recognition to complete...");

            StateResponse response = client.recognizeAndWait(
                imagePath,
                pollInterval * 1000L,
                maxWaitTime * 1000L
            );

            System.out.println();

            if (response.isSuccess()) {
                System.out.println("Request UUID: " + response.getRequestUuid());
                System.out.println("Final State: " + response.getState());
                System.out.println("Created At: " + response.getCreatedAt());
                System.out.println("Updated At: " + response.getUpdatedAt());

                String finalState = response.getState();
                if (("DONE".equals(finalState) || "DELIVERED".equals(finalState)) && response.getResult() != null) {
                    System.out.println();
                    System.out.println("Recognition Result:");
                    System.out.println("-------------------");
                    printResult(response);
                } else if ("FAILED".equals(finalState)) {
                    System.err.println("Recognition failed: " + response.getError());
                }
            } else {
                System.err.println("Recognition request failed!");
                System.err.println("Error: " + response.getError());
            }
        } catch (IOException e) {
            System.err.println("Error during recognition: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Recognition was interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private void printResult(StateResponse response) {
        var result = response.getResult();
        System.out.println("Vehicle Found: " + result.isVehicleFound());

        if (result.isVehicleFound()) {
            System.out.println("Make: " + result.getMake());
            System.out.println("Model: " + result.getModel());
            System.out.println("Generation: " + result.getGeneration());
            System.out.println("Color: " + result.getColor());
            System.out.println("Side: " + result.getSide());
            System.out.println("Angle: " + result.getAngle());
            System.out.println("Recognition Probability: " +
                (result.getRecognitionProbability() != null ?
                    String.format("%.2f%%", result.getRecognitionProbability() * 100) : "N/A"));

            if (result.getRectArea() != null) {
                System.out.println("Bounding Box: " + result.getRectArea());
            }

            System.out.println("Damage Detected: " + result.isDamageDetected());
            if (result.isDamageDetected() && result.getDamageArea() != null) {
                System.out.println("Damage Areas: " + result.getDamageArea());
            }

            System.out.println("Multiple Vehicles: " + result.isMultipleVehiclesInImage());

            if (result.getDetectionNotes() != null) {
                System.out.println("Notes: " + result.getDetectionNotes());
            }
        }
    }

    /**
     * Close resources.
     */
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            // Ignore close errors
        }
    }
}
