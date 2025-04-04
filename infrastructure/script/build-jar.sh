#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { log "Error: logger.sh could not be sourced."; exit 1; }

STACK=${1:-local-compose}

log "Starting Gradle build with stack '$STACK'..."
if ./gradlew clean build -Pstack="$STACK"; then
    log "Gradle build completed successfully."
else
    error "Gradle build failed." >&2
    exit 1
fi
