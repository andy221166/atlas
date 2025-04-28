#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

PROJECT_NAME="atlas-local-compose-simple"
COMPOSE_FILE="$PROJECT_ROOT/deployment/local/compose/backend.yml"
PID_DIR="$PROJECT_ROOT/deployment/local/pids"

source "$PROJECT_ROOT/deployment/util/logger.sh"

# Stop Spring Boot microservices
if [ -d "$PID_DIR" ]; then
    log "Stopping Spring Boot microservices..."
    for pid_file in "$PID_DIR"/*.pid; do
        if [ -f "$pid_file" ]; then
            pid=$(cat "$pid_file")
            service_name=$(basename "$pid_file" .pid)
            if kill -0 "$pid" 2>/dev/null; then
                log "Stopping $service_name (PID $pid)..."
                kill "$pid"
            else
                log "$service_name (PID $pid) is already not running."
            fi
            rm -f "$pid_file"
        fi
    done
    rmdir "$PID_DIR" || true
    log "Spring Boot microservices stopped."
else
    log "No Spring Boot microservices running."
fi

# Stop backend services
log "Stopping backend services..."
if docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" down; then
    log "Backend services stopped successfully."
else
    error "Failed to stop backend services." >&2
    exit 1
fi
