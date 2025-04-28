#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { echo "Error: logger.sh could not be sourced."; exit 1; }

# https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/

# Step 1: Add Kubernetes Dashboard Repository and Update
log "Adding Kubernetes Dashboard Helm repository..."
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
helm repo update

# Step 2: Deploy Kubernetes Dashboard Using Helm
log "Installing/upgrading Kubernetes Dashboard..."
helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard --wait

# Step 3: Generate Bearer Token for Dashboard Admin
log "Applying security configurations..."
kubectl apply -f k8s/dashboard-security.yaml

log "Generating admin bearer token..."
ADMIN_TOKEN=$(kubectl -n kubernetes-dashboard create token dashboard-admin)

# Step 4: Forward Port for Access
log "Forwarding port 8443 to access Kubernetes Dashboard..."
kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443 &

log "Access the Kubernetes Dashboard at: https://localhost:8443"
log "Use the following Bearer Token to log in: $ADMIN_TOKEN"
