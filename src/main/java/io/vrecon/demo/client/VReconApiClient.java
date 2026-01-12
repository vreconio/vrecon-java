package io.vrecon.demo.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vrecon.demo.model.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * REST API client for VRecon Vehicle Recognition API.
 */
public class VReconApiClient {

    private final String baseUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;
    private final boolean verbose;

    public VReconApiClient(String baseUrl, String apiKey, boolean verbose) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.httpClient = HttpClients.createDefault();
        this.verbose = verbose;
    }

    /**
     * Submit an image for vehicle recognition.
     *
     * @param imagePath Path to the image file
     * @return RecognizeResponse containing the request UUID
     * @throws IOException if the request fails
     */
    public RecognizeResponse recognize(String imagePath) throws IOException {
        Path path = Path.of(imagePath);
        if (!Files.exists(path)) {
            throw new IOException("Image file not found: " + imagePath);
        }

        byte[] imageBytes = Files.readAllBytes(path);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String format = getImageFormat(imagePath);

        RecognizeRequest request = new RecognizeRequest(apiKey, base64Image, format);
        String jsonBody = objectMapper.writeValueAsString(request);

        String url = baseUrl + "/api/securapi/recognize";
        if (verbose) {
            System.out.println("[LOG] POST /api/securapi/recognize - Request URL: " + url);
            System.out.println("[LOG] POST /api/securapi/recognize - Request Body: {\"key\":\"***\",\"image\":\"<base64 " + imageBytes.length + " bytes>\",\"format\":\"" + format + "\"}");
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

        return httpClient.execute(httpPost, response -> {
            String responseBody = EntityUtils.toString(response.getEntity());
            if (verbose) {
                System.out.println("[LOG] POST /api/securapi/recognize - Response Status: " + response.getCode());
                System.out.println("[LOG] POST /api/securapi/recognize - Response Body: " + responseBody);
            }
            return objectMapper.readValue(responseBody, RecognizeResponse.class);
        });
    }

    /**
     * Get the state/status of a recognition request.
     *
     * @param requestUuid The UUID returned from the recognize endpoint
     * @return StateResponse containing the current state and result
     * @throws IOException if the request fails
     */
    public StateResponse getState(String requestUuid) throws IOException {
        String jsonResponse = getStateAsJson(requestUuid);
        return parseStateResponse(jsonResponse);
    }

    /**
     * Get the state/status of a recognition request as raw JSON.
     *
     * @param requestUuid The UUID returned from the recognize endpoint
     * @return Raw JSON response string
     * @throws IOException if the request fails
     */
    public String getStateAsJson(String requestUuid) throws IOException {
        StateRequest request = new StateRequest(apiKey, requestUuid);
        String jsonBody = objectMapper.writeValueAsString(request);

        String url = baseUrl + "/api/securapi/state";
        if (verbose) {
            System.out.println("[LOG] POST /api/securapi/state - Request URL: " + url);
            System.out.println("[LOG] POST /api/securapi/state - Request Body: " + jsonBody);
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

        return httpClient.execute(httpPost, response -> {
            String responseBody = EntityUtils.toString(response.getEntity());
            if (verbose) {
                System.out.println("[LOG] POST /api/securapi/state - Response Status: " + response.getCode());
                System.out.println("[LOG] POST /api/securapi/state - Response Body: " + responseBody);
            }
            return responseBody;
        });
    }

    /**
     * Parse a JSON response string into a StateResponse object.
     *
     * @param jsonResponse The JSON response string
     * @return StateResponse parsed from the JSON
     * @throws IOException if parsing fails
     */
    public StateResponse parseStateResponse(String jsonResponse) throws IOException {
        return objectMapper.readValue(jsonResponse, StateResponse.class);
    }

    /**
     * Submit an image and poll for the result until completion.
     *
     * @param imagePath     Path to the image file
     * @param pollInterval  Interval between polls in milliseconds
     * @param maxWaitTime   Maximum time to wait in milliseconds
     * @return StateResponse containing the final result
     * @throws IOException          if the request fails
     * @throws InterruptedException if the polling is interrupted
     */
    public StateResponse recognizeAndWait(String imagePath, long pollInterval, long maxWaitTime)
            throws IOException, InterruptedException {
        RecognizeResponse recognizeResponse = recognize(imagePath);

        if (!recognizeResponse.isSuccess()) {
            StateResponse errorState = new StateResponse();
            errorState.setSuccess(false);
            errorState.setError(recognizeResponse.getError());
            return errorState;
        }

        String requestUuid = recognizeResponse.getRequestUuid();
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < maxWaitTime) {
            StateResponse stateResponse = getState(requestUuid);

            if (!stateResponse.isSuccess()) {
                return stateResponse;
            }

            String state = stateResponse.getState();
            if ("DONE".equals(state) || "DELIVERED".equals(state) || "FAILED".equals(state) || "CANCELLED".equals(state)) {
                return stateResponse;
            }

            Thread.sleep(pollInterval);
        }

        StateResponse timeoutState = new StateResponse();
        timeoutState.setSuccess(false);
        timeoutState.setError("Timeout waiting for recognition result");
        return timeoutState;
    }

    /**
     * Close the HTTP client and release resources.
     */
    public void close() throws IOException {
        httpClient.close();
    }

    private String getImageFormat(String imagePath) {
        String lower = imagePath.toLowerCase();
        if (lower.endsWith(".png")) {
            return "png";
        } else if (lower.endsWith(".webp")) {
            return "webp";
        }
        return "jpg";
    }
}
