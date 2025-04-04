#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { echo "Error: logger.sh could not be sourced."; exit 1; }

# Check if Minikube is already running
if minikube status | grep -q "Running"; then
  echo "Minikube is already running!"
else
  echo "Starting Minikube..."
  minikube start
fi

log "Forwarding gateway-server port..."
kubectl -n default port-forward svc/gateway-server 8080:8080 &

log "Forwarding notification-service port..."
kubectl -n default port-forward svc/notification-service 8084:8084 &
