# Start with a JDK base image
FROM openjdk:17-jdk-slim

# Install FFmpeg and curl
RUN apt-get update && \
    apt-get install -y ffmpeg curl unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install AWS CLI v2
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
unzip awscliv2.zip && \
./aws/install && \
rm -rf awscliv2.zip aws/

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/*.jar app.jar
# COPY src/main/resources/application-docker.yml /app/config/application.yml
COPY src/main/resources/video_process_success_email.md /app/templates/video_process_success_email.md

# Run the JAR
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

CMD ["--spring.config.location=file:/app/config/application.yml"]

# Test the installation locations
# docker run --rm your-image-name which ffmpeg
# docker run --rm your-image-name which ffprobe

# # Or check if they work
# docker run --rm your-image-name ffmpeg -version
# docker run --rm your-image-name ffprobe -version

# GROUP_ID=group2;RESOLUTION=360p
# -DGROUP_ID=group2 -DRESOLUTION=360p -Dspring.devtools.livereload.enabled=false

# docker run --rm \
#   -e JAVA_OPTS="-DGROUP_ID=group2 -DRESOLUTION=360p -Dspring.devtools.livereload.enabled=false" \
#   my-java-app

#   docker run --rm \
#   -e GROUP_ID=group2 \
#   -e RESOLUTION=360p \
#   -e JAVA_OPTS="-DGROUP_ID=$GROUP_ID -DRESOLUTION=$RESOLUTION" \
#   my-java-app


# TO CHECK THE PATHS OF FFMPGE AND FFPROBE IN THE CONTAINER
# Get interactive shell in your container
# docker run -it --rm --entrypoint /bin/bash niharikadutta/processor-service:v0.0.1

# Once inside the container, check paths:
# which ffmpeg
# which ffprobe
# ls -la /usr/bin/ffmpeg
# ls -la /usr/bin/ffprobe

# Check if ffmpeg exists and get its path
# docker run --rm --entrypoint /bin/bash niharikadutta/processor-service:v0.0.1 -c "which ffmpeg"
# docker run --rm --entrypoint /bin/bash niharikadutta/processor-service:v0.0.1 -c "which ffprobe"

# Test if they work
# docker run --rm --entrypoint /bin/bash niharikadutta/processor-service:v0.0.1 -c "ffmpeg -version"
# docker run --rm --entrypoint /bin/bash niharikadutta/processor-service:v0.0.1 -c "ffprobe -version"


# to check the aws credentials in the container
# docker exec -it processor-service-240p env | grep AWS
# docker exec -it processor-service-240p aws s3 ls

# docker exec -it processor-service-240p bash

# localPath: /app/videos/1/9
# remotePath: s3://demobucket-890291224/videos/1/9