#!/bin/bash

PROJECT_NAME="atlas"
REMOVE_VOLUMES=false

# Parse command-line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --clear-data) REMOVE_VOLUMES=true ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

if [ "$REMOVE_VOLUMES" = true ]; then
    echo "Stopping and removing containers..."
    if docker-compose -f docker-compose/docker-compose.yml -p "$PROJECT_NAME" down -v; then
        echo "Containers and volumes stopped and removed successfully."
    else
        echo "Failed to stop and remove containers and volumes." >&2
        exit 1
    fi
fi

# Wait for containers to stop
echo "Waiting for containers to stop completely..."
while [ "$(docker ps -q -f "name=$PROJECT_NAME")" ]; do
    sleep 1
done
echo "All containers have stopped completely."

echo "Starting services..."
if docker-compose -f docker-compose/docker-compose.yml -p "$PROJECT_NAME" up -d; then
    echo "Services started successfully."
else
    echo "Failed to start services." >&2
    exit 1
fi
