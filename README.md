# vrecon.io demo client

Java command-line client for the VRecon Vehicle Recognition REST API.

## Requirements

- Java 17+
- Maven 3.6+

## Build

```bash
./build.sh
```

## Usage

```bash
./vrecon.sh <command> [options]
```

### Commands

| Command | Description |
|---------|-------------|
| `recognize` | Submit an image for vehicle recognition |
| `state` | Get the state of a recognition request |
| `recognize-wait` | Submit an image and poll until complete |
| `help` | Show help message |

### Common Options

| Option | Description |
|--------|-------------|
| `-k, --key <apiKey>` | API key for authentication (required) |
| `--url <baseUrl>` | Base URL of the API (default: https://vrecon.io) |

## Examples

### Submit an image for recognition

```bash
./vrecon.sh recognize --key vrecon_abc123... --image /path/to/vehicle.jpg
```

Output:
```
Submitting image for recognition: /path/to/vehicle.jpg

Recognition request submitted successfully!
Request UUID: 550e8400-e29b-41d4-a716-446655440000
Initial State: PENDING

Use the 'state' command to check the recognition status:
  java -jar vrecon-demo-client.jar state --uuid 550e8400-e29b-41d4-a716-446655440000
```

### Check recognition status

```bash
./vrecon.sh state --key vrecon_abc123... --uuid 550e8400-e29b-41d4-a716-446655440000
```

Output:
```
Request UUID: 550e8400-e29b-41d4-a716-446655440000
State: DONE
Created At: 2026-01-07T12:00:00.000Z
Updated At: 2026-01-07T12:00:10.000Z

Recognition Result:
-------------------
Vehicle Found: true
Make: Toyota
Model: Camry
Generation: 2020-2024
Color: White
Side: front-left
Angle: level
Recognition Probability: 94.00%
Bounding Box: RectArea{x=100, y=50, width=400, height=300}
Damage Detected: false
Multiple Vehicles: false
```

### Submit and wait for result

```bash
./vrecon.sh recognize-wait --key vrecon_abc123... --image /path/to/vehicle.jpg
```

Additional options:
- `--poll-interval <seconds>` - Polling interval (default: 2)
- `--timeout <seconds>` - Max wait time (default: 60)

### Use custom API URL

```bash
./vrecon.sh recognize --url https://api.vrecon.io --key vrecon_abc123... --image vehicle.jpg
```

## Recognition States

| State | Description |
|-------|-------------|
| `PENDING` | Request received, waiting to be processed |
| `PROCESSING` | Currently being analyzed |
| `DONE` | Recognition completed successfully |
| `FAILED` | Recognition failed |

## Project Structure

```
java/
├── build.sh                 # Build script
├── vrecon.sh                # CLI wrapper
├── pom.xml                  # Maven configuration
└── src/main/java/io/vrecon/demo/
    ├── VReconDemoApp.java           # Main application
    ├── client/
    │   └── VReconApiClient.java     # REST API client
    ├── commands/
    │   └── VReconCommands.java      # Command handlers
    └── model/                       # Request/Response DTOs
        ├── RecognizeRequest.java
        ├── RecognizeResponse.java
        ├── StateRequest.java
        ├── StateResponse.java
        └── RecognitionResult.java
```
