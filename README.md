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

## Getting started

Step 1: Start MySQL, Redis, Kafka

### For Spring Boot stack

Step 2: Build project

```bash
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home clean build
```

Step 3: Run applications

```bash
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :edge.discovery-server.eureka:bootRun
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :application.spring-boot.user:bootRun
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :application.spring-boot.product:bootRun
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :application.spring-boot.order:bootRun
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :application.spring-boot.notification:bootRun
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :edge.auth-server.spring-security-jwt:bootRun
./gradlew -Dorg.gradle.java.home=/path/to/your-jdk-17-home :edge.api-gateway.spring-cloud-gateway:bootRun
```

---

## Installation

### Build the Project

#### Step 1: Build JAR files

```bash
./script/build-jar.sh
```

The default profile is `local-compose`.

If you want to change the profile, you can pass param as below:

```bash
./script/build-jar.sh local-k8s
./script/build-jar.sh aws
```

#### Step 2: Build Docker images

```bash
./script/build-docker-image.sh
```

### Running the Project Locally

You have multiple options to start the project locally.

#### Using Docker Compose

```bash
./script/local-compose-fully-start.sh
```

#### Using Kubernetes (Minikube)

Set up and run the project using Minikube with the following commands:

```bash
# Setup
./script/k8s-init-minikube-cluster.sh
./script/k8s-init-dashboard.sh
./script/k8s-init-backend.sh
# Wait for approximately 5 minutes...
./script/k8s-init-services.sh

# Start the services
./script/k8s-start.sh
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

---

## TODO

- [ ] Resiliency patterns
- [ ] Export aggOrders function on frontend
- [ ] Feature Toggle. Ref: https://medium.com/@uptoamir/refresh-configurations-at-runtime-with-spring-cloud-bus-a-practical-guide-38a7f739eca6
