FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR file into the container
COPY target/*.jar app.jar

# Run JAR with default config
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
CMD ["--spring.config.location=file:/app/config/application.yml"]
