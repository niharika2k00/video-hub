# Start with a JDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
# COPY src/main/resources/application-docker.yml /app/config/application.yml
COPY target/*.jar app.jar

# Run the JAR
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

CMD ["--spring.config.location=file:/app/config/application.yml"]
