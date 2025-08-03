# Start with a JDK base image
FROM openjdk:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 4040

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]