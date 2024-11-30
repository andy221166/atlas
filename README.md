# Atlas

## Project Overview

**Atlas** is a microservices-based platform designed to manage the business logic of a basic storefront application.

## Architecture

- **Microservices**: Modular architecture for scalability and flexibility.
- **Hexagonal Architecture**: Separation of concerns to enhance maintainability.
- **CQRS (Command Query Responsibility Segregation)**: Separation of read and write operations for better performance and scalability.
- **Cross-Cutting Concerns**: Centralized handling of concerns such as logging, security, and monitoring.

## Technologies Used

- **Java 17**: Core programming language for backend services.
- **Spring Boot 3.2.5**: Framework for developing robust microservices.
- **Spring Cloud**: Tools for building and managing distributed systems.
- **MySQL 8, Redis, Zookeeper, Kafka**: Backend technologies for database and messaging.
- **Maven**: Dependency management and project building.
- **Docker**: Containerization for deploying services.
- **Kubernetes (Optional)**: Orchestrates service deployments in a cluster.
- **Third-party Services**: Integration with external APIs or tools.

## Installation

### Build the Project

To build Maven modules and Docker images, run the following command:

```bash
./scripts/build.sh
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

## Testing

You can test the APIs using the web app built with Vue 3. 

To start the frontend application, follow these steps:

```bash
cd frontend
npm run serve
```

The web application will then be accessible at http://localhost:9000.
