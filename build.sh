#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "Building VRecon Demo Client..."
mvn clean package -q

JAR_NAME="vrecon-demo-client.jar"
SOURCE_JAR="target/vrecon-demo-client-1.0.0-SNAPSHOT.jar"

if [ -f "$SOURCE_JAR" ]; then
    cp "$SOURCE_JAR" "$JAR_NAME"
    echo "Build successful: $JAR_NAME"
else
    echo "Error: JAR not found at $SOURCE_JAR"
    exit 1
fi
