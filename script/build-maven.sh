#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { log "Error: logger.sh could not be sourced."; exit 1; }

log "Starting Maven build..."
if mvn clean install; then
    log "Maven build completed successfully."
else
    error "Maven build failed." >&2
    exit 1
fi
