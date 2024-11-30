#!/bin/bash

# Step 1: Build the Maven project

echo "Starting Maven build..."
if mvn clean install; then
    echo "Maven build completed successfully."
else
    echo "Maven build failed." >&2
    exit 1
fi

# Step 2: Build Docker images for each service

echo "Building Docker image for user-service..."
if docker build -t user-service ./service/user-service/user-application; then
    echo "Built Docker image for user-service successfully."
else
    echo "Failed to build Docker image for user-service." >&2
    exit 1
fi

echo "Building Docker image for product-service..."
if docker build -t product-service ./service/product-service/product-application; then
    echo "Built Docker image for product-service successfully."
else
    echo "Failed to build Docker image for product-service." >&2
    exit 1
fi

echo "Building Docker image for order-service..."
if docker build -t order-service ./service/order-service/order-application; then
    echo "Built Docker image for order-service successfully."
else
    echo "Failed to build Docker image for order-service." >&2
    exit 1
fi

echo "Building Docker image for report-service..."
if docker build -t report-service ./service/report-service/report-application; then
    echo "Built Docker image for report-service successfully."
else
    echo "Failed to build Docker image for report-service." >&2
    exit 1
fi

echo "Building Docker image for notification-service..."
if docker build -t notification-service ./service/notification-service; then
    echo "Built Docker image for notification-service successfully."
else
    echo "Failed to build Docker image for notification-service." >&2
    exit 1
fi

echo "Building Docker image for task-service..."
if docker build -t task-service ./service/task-service/task-application; then
    echo "Built Docker image for task-service successfully."
else
    echo "Failed to build Docker image for task-service." >&2
    exit 1
fi

echo "Building Docker image for eureka-server..."
if docker build -t eureka-server ./edge/discovery-server/eureka-server; then
    echo "Built Docker image for eureka-server successfully."
else
    echo "Failed to build Docker image for eureka-server." >&2
    exit 1
fi

echo "Building Docker image for gateway-server..."
if docker build -t gateway-server ./edge/gateway-server; then
    echo "Built Docker image for gateway-server successfully."
else
    echo "Failed to build Docker image for gateway-server." >&2
    exit 1
fi

echo "Pruning unused Docker images..."
if docker image prune -f; then
    echo "Pruned Unused Docker images successfully."
else
    echo "Failed to prune Docker images." >&2
    exit 1
fi
