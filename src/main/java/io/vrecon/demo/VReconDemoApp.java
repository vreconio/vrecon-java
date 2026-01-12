package io.vrecon.demo;

import io.vrecon.demo.commands.VReconCommands;
import org.apache.commons.cli.*;

/**
 * VRecon Demo Application - CLI for Vehicle Recognition REST API.
 *
 * Usage:
 *   recognize  - Submit an image for vehicle recognition
 *   state      - Get the state of a recognition request
 *   recognize-wait - Submit an image and wait for the result
 */
public class VReconDemoApp {

    private static final String DEFAULT_BASE_URL = "https://vrecon.io";
    private static final int DEFAULT_POLL_INTERVAL = 2;
    private static final int DEFAULT_MAX_WAIT_TIME = 60;

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String command = args[0];
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

        switch (command) {
            case "recognize" -> handleRecognize(commandArgs);
            case "state" -> handleState(commandArgs);
            case "recognize-wait" -> handleRecognizeWait(commandArgs);
            case "help", "-h", "--help" -> printUsage();
            default -> {
                System.err.println("Unknown command: " + command);
                System.err.println();
                printUsage();
                System.exit(1);
            }
        }
    }

    private static void handleRecognize(String[] args) {
        Options options = createCommonOptions();
        options.addOption(Option.builder("i")
            .longOpt("image")
            .hasArg()
            .required()
            .desc("Path to the image file")
            .build());

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            String baseUrl = cmd.getOptionValue("url", DEFAULT_BASE_URL);
            String apiKey = cmd.getOptionValue("key");
            String imagePath = cmd.getOptionValue("image");
            boolean verbose = cmd.hasOption("verbose");

            VReconCommands commands = new VReconCommands(baseUrl, apiKey, verbose);
            try {
                commands.recognize(imagePath);
            } finally {
                commands.close();
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printCommandHelp("recognize", options);
            System.exit(1);
        }
    }

    private static void handleState(String[] args) {
        Options options = createCommonOptions();
        options.addOption(Option.builder("u")
            .longOpt("uuid")
            .hasArg()
            .required()
            .desc("Request UUID to check status")
            .build());
        options.addOption(Option.builder("j")
            .longOpt("json")
            .desc("Output raw JSON response")
            .build());

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            String baseUrl = cmd.getOptionValue("url", DEFAULT_BASE_URL);
            String apiKey = cmd.getOptionValue("key");
            String requestUuid = cmd.getOptionValue("uuid");
            boolean verbose = cmd.hasOption("verbose");
            boolean json = cmd.hasOption("json");

            VReconCommands commands = new VReconCommands(baseUrl, apiKey, verbose);
            try {
                commands.getState(requestUuid, json);
            } finally {
                commands.close();
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printCommandHelp("state", options);
            System.exit(1);
        }
    }

    private static void handleRecognizeWait(String[] args) {
        Options options = createCommonOptions();
        options.addOption(Option.builder("i")
            .longOpt("image")
            .hasArg()
            .required()
            .desc("Path to the image file")
            .build());
        options.addOption(Option.builder("p")
            .longOpt("poll-interval")
            .hasArg()
            .desc("Poll interval in seconds (default: " + DEFAULT_POLL_INTERVAL + ")")
            .build());
        options.addOption(Option.builder("t")
            .longOpt("timeout")
            .hasArg()
            .desc("Max wait time in seconds (default: " + DEFAULT_MAX_WAIT_TIME + ")")
            .build());

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            String baseUrl = cmd.getOptionValue("url", DEFAULT_BASE_URL);
            String apiKey = cmd.getOptionValue("key");
            String imagePath = cmd.getOptionValue("image");
            int pollInterval = Integer.parseInt(cmd.getOptionValue("poll-interval",
                String.valueOf(DEFAULT_POLL_INTERVAL)));
            int maxWaitTime = Integer.parseInt(cmd.getOptionValue("timeout",
                String.valueOf(DEFAULT_MAX_WAIT_TIME)));
            boolean verbose = cmd.hasOption("verbose");

            VReconCommands commands = new VReconCommands(baseUrl, apiKey, verbose);
            try {
                commands.recognizeAndWait(imagePath, pollInterval, maxWaitTime);
            } finally {
                commands.close();
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printCommandHelp("recognize-wait", options);
            System.exit(1);
        }
    }

    private static Options createCommonOptions() {
        Options options = new Options();
        options.addOption(Option.builder("k")
            .longOpt("key")
            .hasArg()
            .required()
            .desc("API key for authentication")
            .build());
        options.addOption(Option.builder()
            .longOpt("url")
            .hasArg()
            .desc("Base URL of the VRecon API (default: " + DEFAULT_BASE_URL + ")")
            .build());
        options.addOption(Option.builder("v")
            .longOpt("verbose")
            .desc("Enable verbose logging for API requests")
            .build());
        return options;
    }

    private static void printUsage() {
        System.out.println("VRecon Demo Client - Vehicle Recognition REST API");
        System.out.println();
        System.out.println("Usage: java -jar vrecon-demo-client.jar <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  recognize       Submit an image for vehicle recognition");
        System.out.println("  state           Get the state of a recognition request");
        System.out.println("  recognize-wait  Submit an image and wait for the result");
        System.out.println("  help            Show this help message");
        System.out.println();
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  Submit an image for recognition:");
        System.out.println("    java -jar vrecon-demo-client.jar recognize \\");
        System.out.println("      --key vrecon_abc123... --image /path/to/vehicle.jpg");
        System.out.println();
        System.out.println("  Check recognition status:");
        System.out.println("    java -jar vrecon-demo-client.jar state \\");
        System.out.println("      --key vrecon_abc123... --uuid 550e8400-e29b-41d4-a716-446655440000");
        System.out.println();
        System.out.println("  Submit and wait for result:");
        System.out.println("    java -jar vrecon-demo-client.jar recognize-wait \\");
        System.out.println("      --key vrecon_abc123... --image /path/to/vehicle.jpg \\");
        System.out.println("      --poll-interval 2 --timeout 60");
        System.out.println();
        System.out.println("Common Options:");
        System.out.println("  -k, --key <apiKey>   API key for authentication (required)");
        System.out.println("  --url <baseUrl>      Base URL of the API (default: " + DEFAULT_BASE_URL + ")");
        System.out.println("  -v, --verbose        Enable verbose logging for API requests");
    }

    private static void printCommandHelp(String command, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar vrecon-demo-client.jar " + command, options);
    }
}
