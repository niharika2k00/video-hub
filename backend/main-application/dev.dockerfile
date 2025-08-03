# Start with a JDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY src/main/resources/application-docker.yml /app/config/application.yml
COPY target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 4040

# Run the JAR  java -jar <jar_file_path>
ENTRYPOINT ["java", "-jar", "app.jar"]

# Default arguments to ENTRYPOINT
CMD ["--spring.config.location=file:/app/config/application.yml"]

#  cd main-application

# docker build -f dev.dockerfile -t main-application:v0.0.1 .
# docker build -f dev.dockerfile -t processor-service:v0.0.1 .
# docker build -f dev.dockerfile -t email-service:v0.0.1 .

# docker run -p 4040:4040 main-application:v0.0.1

