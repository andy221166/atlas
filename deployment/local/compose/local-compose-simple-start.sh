#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

PROJECT_NAME="atlas-local-compose-simple"
BACKEND_SERVICES=(mysql redis kafka)
COMPOSE_FILE="$PROJECT_ROOT/deployment/local/compose/backend.yml"
JAVA_HOME="C:/Users/R16108/.jdks/corretto-17.0.14"
GRADLEW="$PROJECT_ROOT/gradlew"
PID_DIR="$PROJECT_ROOT/deployment/local/compose/pids"

source "$PROJECT_ROOT/deployment/util/logger.sh"

# Start backend services
if [ "${#BACKEND_SERVICES[@]}" -eq 0 ]; then
    error "No backend services specified to start."
    exit 1
fi

log "Starting backend services: ${BACKEND_SERVICES[*]}..."
docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" up -d "${BACKEND_SERVICES[@]}"
log "Backend services started successfully."

# Start Spring Boot microservices
log "Starting Spring Boot microservices..."
cd "$PROJECT_ROOT"
mkdir -p "$PID_DIR"

start_microservice() {
  local module=$1
  local pid_file=$2
  "$GRADLEW" -Dorg.gradle.java.home="$JAVA_HOME" "$module" &
  echo $! > "$pid_file"
}

(
  start_microservice ":edge.discovery-server.eureka:bootRun" "$PID_DIR/edge.discovery-server.eureka.pid"
  start_microservice ":application.spring-boot.user:bootRun" "$PID_DIR/application.spring-boot.user.pid"
  # start_microservice ":application.spring-boot.product:bootRun" "$PID_DIR/application.spring-boot.product.pid"
  # start_microservice ":application.spring-boot.order:bootRun" "$PID_DIR/application.spring-boot.order.pid"
  # start_microservice ":application.spring-boot.notification:bootRun" "$PID_DIR/application.spring-boot.notification.pid"
  start_microservice ":edge.auth-server.spring-security-jwt:bootRun" "$PID_DIR/edge.auth-server.spring-security-jwt.pid"
  start_microservice ":edge.api-gateway.spring-cloud-gateway:bootRun" "$PID_DIR/edge.api-gateway.spring-cloud-gateway.pid"
  wait
)

log "Spring Boot m0icroservices started successfully."
