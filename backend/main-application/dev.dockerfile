# Start with a JDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/*.jar app.jar
COPY src/main/resources/new_contact_form_submission_email.md /app/templates/new_contact_form_submission_email.md
COPY src/main/resources/contact_form_acknowledgement_email.md /app/templates/contact_form_acknowledgement_email.md
COPY src/main/resources/user_welcome_email.md /app/templates/user_welcome_email.md

# Copied from local repository when running in local machine
# COPY src/main/resources/application-docker.yml /app/config/application.yml

# Expose the port your app runs on
EXPOSE 4040

# Run the JAR  java -jar <jar_file_path>
ENTRYPOINT ["java", "-jar", "app.jar"]

# Default arguments to ENTRYPOINT
CMD ["--spring.config.location=file:/app/config/application.yml"]



# mvn clean install && mvn clean package
# docker build -f dev.dockerfile -t main-application:v0.0.1 .
# docker build -f dev.dockerfile -t processor-service:v0.0.1 .
# docker build -f dev.dockerfile -t email-service:v0.0.1 .

# docker run -p 4040:4040 main-application:v0.0.1

#  if we send the config (application.yml) as a volume, it will override the default config inside the image
#  passing spring.config.location via CMD will override docker build -f dev.dockerfile -t email-service:v0.0.1 .the original src/main/resources/application.yml in the docker image means it will take the one passed via CMD
#
# Spring find it automatically as its the default config path, but we can't use it as a volume since using the image JAR file
#  ./config/application.yml:/app/src/main/resources/application.yml:ro


# docker buildx build --platform linux/amd64,linux/arm64 -f dev.dockerfile -t niharikadutta/processor-service:v0.0.1 --push .


#   docker buildx build \
#   --platform linux/amd64,linux/arm64 \
#   -f dev.dockerfile \
#   -t niharikadutta/processor-service:v0.0.1 \
#   --push .

# Build for both architectures (without pushing)
# docker buildx build --platform linux/amd64,linux/arm64 \
#   -t niharikadutta/main-application:v0.0.1 \
#   -f main-application/dev.dockerfile main-application

# Then push manually
# docker push niharikadutta/main-application:v0.0.1




# Push to Docker Hub:
# docker push niharikadutta/main-application:v0.0.1
# docker push niharikadutta/processor-service:v0.0.1
# docker push niharikadutta/email-service:v0.0.1

# On your EC2, pull the new images:
# docker pull niharikadutta/main-application:v0.0.1
# docker pull niharikadutta/processor-service:v0.0.1
# docker pull niharikadutta/email-service:v0.0.1

# http://18.219.151.181:4040/api/test

# docker tag main-application:v0.0.1 <docker_username>/main-application:v0.0.1
# docker push <docker_username>/main-application:v0.0.1
# docker tag my-app:latest myusername/my-app-repo:latest


# now open another terminal and run the following commands:

#   cd ./deployment/docker-mysql
#   docker compose up -d
#   docker ps




