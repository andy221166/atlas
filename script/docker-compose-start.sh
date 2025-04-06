#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { echo "Error: logger.sh could not be sourced."; exit 1; }

PROJECT_NAME="atlas"
REMOVE_VOLUMES=false

# Define the list of services to start
SERVICES=(
  "mysql"
  "redis"
#  "zookeeper"
  "kafka"
#  "rabbitmq"
#  "loki"
#  "promtail"
#  "prometheus"
#  "zipkin"
#  "grafana"
#  "smtp4dev"
#  "eureka-server"
#  "user-service"
#  "product-service"
#  "order-service"
#  "notification-service"
#  "report-service"
#  "task-service"
#  "gateway-server"
)

# Parse command-line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --clear-data) REMOVE_VOLUMES=true ;;
        *) error "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

if [ "$REMOVE_VOLUMES" = true ]; then
    log "Stopping and removing containers..."
    if docker-compose -f deployment/local/docker-compose/docker-compose.yml -p "$PROJECT_NAME" down -v; then
        log "Containers and volumes stopped and removed successfully."
    else
        error "Failed to stop and remove containers and volumes." >&2
        exit 1
    fi
fi

# Wait for containers to stop
log "Waiting for containers to stop completely..."
while [ "$(docker ps -q -f "name=$PROJECT_NAME")" ]; do
    sleep 1
done
log "All containers have stopped completely."

# Start specified services
if [ "${#SERVICES[@]}" -eq 0 ]; then
    log "No services specified to start."
    exit 1
fi

log "Starting specified services: ${SERVICES[*]}..."
if docker-compose -f deployment/local/docker-compose/docker-compose.yml -p "$PROJECT_NAME" up -d "${SERVICES[@]}"; then
    log "Specified services started successfully."
else
    error "Failed to start specified services." >&2
    exit 1
fi

# Remove dangling images
docker images -f "dangling=true" -q | xargs -r docker rmi
