#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { log "Error: logger.sh could not be sourced."; exit 1; }

# Step 1: Build the Maven project

log "Starting Maven build..."
if mvn clean install; then
    log "Maven build completed successfully."
else
    error "Maven build failed." >&2
    exit 1
fi

# Step 2: Build Docker images for each service

log "Building Docker image for user-service..."
if docker build -t user-service ./service/user-service/user-application; then
    log "Built Docker image for user-service successfully."
else
    error "Failed to build Docker image for user-service." >&2
    exit 1
fi

log "Building Docker image for product-service..."
if docker build -t product-service ./service/product-service/product-application; then
    log "Built Docker image for product-service successfully."
else
    error "Failed to build Docker image for product-service." >&2
    exit 1
fi

log "Building Docker image for order-service..."
if docker build -t order-service ./service/order-service/order-application; then
    log "Built Docker image for order-service successfully."
else
    error "Failed to build Docker image for order-service." >&2
    exit 1
fi

log "Building Docker image for report-service..."
if docker build -t report-service ./service/report-service/report-application; then
    log "Built Docker image for report-service successfully."
else
    error "Failed to build Docker image for report-service." >&2
    exit 1
fi

log "Building Docker image for notification-service..."
if docker build -t notification-service ./service/notification-service; then
    log "Built Docker image for notification-service successfully."
else
    error "Failed to build Docker image for notification-service." >&2
    exit 1
fi

log "Building Docker image for task-service..."
if docker build -t task-service ./service/task-service/task-application; then
    log "Built Docker image for task-service successfully."
else
    error "Failed to build Docker image for task-service." >&2
    exit 1
fi

log "Building Docker image for eureka-server..."
if docker build -t eureka-server ./edge/discovery-server/eureka-server; then
    log "Built Docker image for eureka-server successfully."
else
    error "Failed to build Docker image for eureka-server." >&2
    exit 1
fi

log "Building Docker image for gateway-server..."
if docker build -t gateway-server ./edge/gateway-server; then
    error "Built Docker image for gateway-server successfully."
else
    log "Failed to build Docker image for gateway-server." >&2
    exit 1
fi

log "Pruning unused Docker images..."
if docker image prune -f; then
    log "Pruned Unused Docker images successfully."
else
    error "Failed to prune Docker images." >&2
    exit 1
fi
