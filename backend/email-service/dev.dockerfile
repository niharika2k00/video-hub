# Start with a JDK base image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY src/main/resources/application-docker.yml /app/config/application.yml
COPY target/*.jar app.jar

# Run the JAR
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

CMD ["--spring.config.location=file:/app/config/application.yml"]



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

