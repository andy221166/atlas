# Atlas

## Project Overview

**Atlas** is a microservices-based platform designed to handle business operations such as order management, user services, and more. The project leverages Spring Boot, Java 17, and various other modern technologies to create a scalable and maintainable architecture.

## Features

- Order management (place, import, export orders)
- User management and authentication
- Containerized services with Docker
- CQRS pattern for handling commands and queries
- Integration with external services and databases

## Technologies Used
- 
- **Java 17**: The programming language for backend services.
- **Spring Boot 3.2.5**: Framework for building microservices.
- **Spring Cloud**: Tools for managing distributed systems.
- **Maven**: Dependency management and project build.
- **Docker**: Containerization for service deployment.
- **Kubernetes (optional)**: Deployment orchestration.
- **Redis, Kafka, MySQL**: Backend technologies for data and messaging.

## Prerequisites

Ensure you have the following installed on your system:
- **Java 17**
- **Maven 3.x**
- **Docker**
- **MySQL** (or the relevant database you're using)
- **Kafka and Zookeeper**

## Installation and Setup

### Build the Project

To build the project using Maven, run:

```bash
./scripts/build.sh
```

### Running with Docker Compose

```bash
./scripts/start-local.sh
```
