#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_PATH="$SCRIPT_DIR/vrecon-demo-client.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Error: vrecon-demo-client.jar not found."
    echo "Please run ./build.sh first to build the project."
    exit 1
fi

java -jar "$JAR_PATH" "$@"
