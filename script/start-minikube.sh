#!/bin/bash

# Check if Minikube is running, and start if not
check_minikube_running() {
    if minikube status | grep -q 'host: Running'; then
        echo "Minikube is already running."
    else
        echo "Minikube is not running. Starting Minikube..."
        minikube start
    fi
}

# Function to check if an image exists in Minikube
delete_image() {
    local image_name=$1
    if minikube image ls | grep -q "$image_name"; then
        echo "Image $image_name exists in Minikube. Deleting it..."
        minikube image rm "$image_name"
    else
        echo "Image $image_name does not exist in Minikube."
    fi
}

check_minikube_running

kubectl apply -f k8s/mysql.yaml
kubectl apply -f k8s/redis.yaml
kubectl apply -f k8s/zookeeper.yaml
kubectl apply -f k8s/kafka.yaml
kubectl apply -f k8s/zipkin.yaml

for service in user-service product-service order-service notification-service gateway-server; do
    # Delete the Kubernetes service if it exists
    kubectl delete -f k8s/$service.yaml || true

    # Check if the image exists and delete it if it does
    delete_image "$service:latest"

    # Load the new image into Minikube
    echo "Loading $service image into Minikube..."
    minikube image load "$service:latest"

    # Apply the Kubernetes manifest for the service
    echo "Applying $service.yaml..."
    kubectl apply -f k8s/$service.yaml
done
