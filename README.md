# Atlas

## Project Overview

**Atlas** is a microservices-based platform.

---

## Architecture

- **Microservices**: Modular architecture for scalability and flexibility.
- **Hexagonal Architecture**: Separation of concerns to enhance maintainability.
- **CQRS (Command Query Responsibility Segregation)**: Separation of read and write operations for better performance and scalability.
- **Cross-Cutting Concerns**: Centralized handling of concerns such as logging, security, and monitoring.

---

## Technical Stack

---

## Installation

### Build the Project

#### Step 1: Build JAR files

```bash
./scripts/build-jar.sh
```

The default profile is `local-compose`.

If you want to change the profile, you can pass param as below:

```bash
./scripts/build-jar.sh local-k8s
./scripts/build-jar.sh aws
```

#### Step 2: Build Docker images

```bash
./scripts/build-docker-image.sh
```

### Running the Project Locally

You have multiple options to start the project locally.

#### Using Docker Compose

```bash
./scripts/docker-compose-start.sh
```

#### Using Kubernetes (Minikube)

Set up and run the project using Minikube with the following commands:

```bash
# Setup
./scripts/k8s-init-minikube-cluster.sh
./scripts/k8s-init-dashboard.sh
./scripts/k8s-init-backend.sh
# Wait for approximately 5 minutes...
./scripts/k8s-init-services.sh

# Start the services
./scripts/k8s-start.sh
```

---

## Testing

You can test the APIs using the web app built with Vue 3. 

To start the frontend application, follow these steps:

```bash
cd frontend
npm run serve
```

The web application will then be accessible at http://localhost:9000.
