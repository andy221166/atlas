# Builder Stage: Extract application layers
FROM openjdk:17-slim AS builder

# Set working directory
WORKDIR /app

# Copy the application JAR to the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Use Spring Boot's layertools to extract layers
RUN java -Djarmode=layertools -jar app.jar extract

# Runtime Stage: Create thin image
FROM openjdk:17-slim

# Set working directory
WORKDIR /app

# Copy extracted layers from the builder stage
COPY --from=builder /app/dependencies/ ./dependencies/
COPY --from=builder /app/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder /app/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder /app/application/ ./application/

# Expose application port
EXPOSE 8084

# Run the application using Spring Boot loader
ENTRYPOINT ["java", "-cp", "dependencies/:snapshot-dependencies/:spring-boot-loader/", "org.springframework.boot.loader.JarLauncher"]
