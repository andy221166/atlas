FROM eclipse-temurin:17-jre-alpine AS builder
WORKDIR extracted
ADD ./build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre-alpine
WORKDIR app
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

# Expose application port
EXPOSE 8081

# Optimized JVM options with heap dump on OOM
ENTRYPOINT ["java", \
  "-Xms512m", \
  "-Xmx1g", \
  "-XX:+UseG1GC", \
  "-XX:MaxGCPauseMillis=200", \
  "-XX:+AlwaysPreTouch", \
  "-XX:+HeapDumpOnOutOfMemoryError", \
  "-XX:HeapDumpPath=/tmp/heapdump.hprof", \
  "-Djava.security.egd=file:/dev/urandom", \
  # Since Spring Boot 3.2.0, a new org.springframework.boot.loader.launch.JarLauncher has been introduced
  # instead of org.springframework.boot.loader.JarLauncher
  "org.springframework.boot.loader.launch.JarLauncher"]
