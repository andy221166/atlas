#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

# Get the absolute path to the directory containing this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Get the project root
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

PROJECT_NAME="atlas-local-compose-simple"
REMOVE_VOLUMES=false
JAVA_HOME="C:/Users/R16108/.jdks/corretto-17.0.14"
GRADLEW="$PROJECT_ROOT/gradlew"

# Load logger.sh
source "$PROJECT_ROOT/deployment/util/logger.sh"

# Define the list of services to start
SERVICES=(mysql redis kafka-v2)

# Parse command-line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --clear-data) REMOVE_VOLUMES=true ;;
        *) error "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

# Stop and remove containers and volumes if requested
if [ "$REMOVE_VOLUMES" = true ]; then
    log "Stopping and removing containers..."
    docker-compose -f "$PROJECT_ROOT/deployment/local/backend.yml" -p "$PROJECT_NAME" down -v
    log "Containers and volumes stopped and removed successfully."
fi

# Wait until all related containers are completely stopped
log "Waiting for containers to stop completely..."
while true; do
    running_containers=$(docker ps -q -f "name=$PROJECT_NAME")
    if [ -z "$running_containers" ]; then
        break
    fi
    sleep 1
done
log "All containers have stopped completely."

# Start specified services
if [ "${#SERVICES[@]}" -eq 0 ]; then
    error "No services specified to start."
    exit 1
fi

log "Starting specified services: ${SERVICES[*]}..."
docker-compose -f "$PROJECT_ROOT/deployment/local/compose/backend.yml" -p "$PROJECT_NAME" up -d "${SERVICES[@]}"
log "Specified services started successfully."

# Run microservices in parallel
log "Starting microservices..."
(
  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :discovery-server.eureka-server:bootRun &
#  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :application.spring-boot.user:bootRun &
#  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :application.spring-boot.product:bootRun &
#  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :application.spring-boot.order:bootRun &
#  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :application.spring-boot.notification:bootRun &
#  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :auth-server:auth-server.spring-security-jwt:bootRun &
#  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" :api-gateway.spring-cloud-gateway:bootRun &
  wait
)

log "All microservices started."
