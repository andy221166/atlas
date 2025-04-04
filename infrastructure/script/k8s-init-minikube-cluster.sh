#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { echo "Error: logger.sh could not be sourced."; exit 1; }

# Define Constants for Minikube Resources
CPUS=4           # Number of CPUs to allocate to Minikube
MEMORY=8g        # Amount of memory to allocate to Minikube (e.g., 8g for 8GB)

# Stop and Delete Existing Minikube Cluster
log "Stopping and deleting existing Minikube cluster (if any)..."
minikube stop || true
minikube delete || true

# Start New Minikube Cluster with Specified Resources
log "Starting a new Minikube cluster with $CPUS CPUs and $MEMORY memory..."
minikube start --cpus="$CPUS" --memory="$MEMORY"

# Build or Load the Images into Minikube
IMAGES=("user-service" "product-service" "order-service" "notification-service" "report-service" "task-service" "gateway-server")

log "Loading images into Minikube..."
for IMAGE in "${IMAGES[@]}"; do
  if docker images "$IMAGE" | grep -q "$IMAGE"; then
    log "Loading image: $IMAGE"
    minikube image load "$IMAGE"
  else
    error "Error: Image $IMAGE not found locally. Please build the image first."
    exit 1
  fi
done

log "All images loaded successfully into Minikube!"

# Verify Cluster and Loaded Images
log "Verifying Minikube cluster and loaded images..."
kubectl get nodes || { error "Error: Unable to get Kubernetes nodes. Ensure Minikube cluster is running."; exit 1; }
minikube image list || { error "Error: Unable to list Minikube images. Ensure the images are loaded correctly."; exit 1; }

log "Minikube cluster is ready with $CPUS CPUs, $MEMORY memory, and the specified images. 🎉"
