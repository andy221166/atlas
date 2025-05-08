#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { log "Error: logger.sh could not be sourced."; exit 1; }

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

log "Building Docker image for notification-service..."
if docker build -t notification-service ./service/notification-service; then
    log "Built Docker image for notification-service successfully."
else
    error "Failed to build Docker image for notification-service." >&2
    exit 1
fi

log "Building Docker image for eureka-server..."
if docker build -t eureka-server ../../edge/discovery-server/eureka-server; then
    log "Built Docker image for eureka-server successfully."
else
    error "Failed to build Docker image for eureka-server." >&2
    exit 1
fi

log "Building Docker image for auth-server..."
if docker build -t eureka-server ../../edge/auth-server/auth-server-spring-security-jwt; then
    log "Built Docker image for auth-server successfully."
else
    error "Failed to build Docker image for auth-server." >&2
    exit 1
fi

log "Building Docker image for api-gateway..."
if docker build -t api-gateway ../../edge/api-gateway/api-gateway-spring-cloud-gateway; then
    log "Built Docker image for api-gateway successfully."
else
    error "Failed to build Docker image for api-gateway." >&2
    exit 1
fi

log "Pruning unused Docker images..."
if docker image prune -f; then
    log "Pruned Unused Docker images successfully."
else
    error "Failed to prune Docker images." >&2
    exit 1
fi
