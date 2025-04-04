#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { echo "Error: logger.sh could not be sourced."; exit 1; }

# Define variables
manifests=(
  "security"
  "user-service"
  "product-service"
  "order-service"
  "notification-service"
  "report-service"
  "task-service"
  "gateway-server"
)

# Process each service
for manifest in "${manifests[@]}"; do
    log "Applying deployment/k8s/$manifest.yaml..."

    # Delete the existing Kubernetes resource, ignore errors if not found
    kubectl delete -f deployment/k8s/"$manifest".yaml --ignore-not-found=true

    kubectl apply -f deployment/k8s/"$manifest".yaml || { echo "Failed to apply $manifest"; exit 1; }
done

log "Done apply manifest files"
