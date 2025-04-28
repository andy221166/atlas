#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

# Get the absolute path to the directory containing this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Get the project root (one level up from this script)
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

STACK=${1:-local-compose-simple}
GRADLEW="$PROJECT_ROOT/gradlew"
JAVA_HOME="C:/Users/R16108/.jdks/corretto-17.0.14"

# Load logger.sh
source "$PROJECT_ROOT/deployment/util/logger.sh"

log "Starting Gradle build with stack '$STACK'..."
if (cd "$PROJECT_ROOT" && "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" clean build -Pstack="$STACK"); then
    log "Gradle build completed successfully."
else
    error "Gradle build failed." >&2
    exit 1
fi
