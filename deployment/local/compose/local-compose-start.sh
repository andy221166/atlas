#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
PROJECT_NAME="${PROJECT_NAME:-atlas-local-compose}"
COMPOSE_FILE="${COMPOSE_FILE:-$PROJECT_ROOT/deployment/local/compose/docker-compose.yml}"

# Service categories
APP_SERVICES=(discovery-server user-service product-service order-service notification-service auth-server api-gateway)
INFRA_SERVICES=(mysql redis kafka)
OBSERVABILITY_SERVICES=(loki promtail prometheus zipkin grafana)

# Check for logger script
if [ ! -f "$PROJECT_ROOT/deployment/util/logger.sh" ]; then
    echo "Error: logger.sh not found at $PROJECT_ROOT/deployment/util/logger.sh"
    exit 1
fi
source "$PROJECT_ROOT/deployment/util/logger.sh"

# Function to print usage
usage() {
    echo "Usage: $0 {app|infra|observability|all}"
    echo "  app: Start application microservices"
    echo "  infra: Start database, cache, queue, etc."
    echo "  observability: Start observability tools"
    exit 1
}

# Validate input
if [ $# -ne 1 ]; then
    error "Exactly one argument is required."
    usage
fi

# Determine services to start based on input
case "$1" in
    app)
        SERVICES=("${APP_SERVICES[@]}")
        ;;
    infra)
        SERVICES=("${INFRA_SERVICES[@]}")
        ;;
    observability)
        # Observability tools like zipkin depend on mysql
        SERVICES=("${OBSERVABILITY_SERVICES[@]}")
        ;;
    *)
        error "Invalid argument: $1"
        usage
        ;;
esac

# Validate services
if [ "${#SERVICES[@]}" -eq 0 ]; then
    error "No services specified to start."
    exit 1
fi

# Start services
log "Starting services: ${SERVICES[*]}..."
if ! docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" up -d "${SERVICES[@]}"; then
    error "Failed to start services."
    exit 1
fi

# Check service status
log "Checking service status..."
for service in "${SERVICES[@]}"; do
    if ! docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" ps "$service" | grep -q "Up"; then
        error "Service $service is not running."
        exit 1
    fi
    log "Service $service is running."
done

log "Services started successfully."
