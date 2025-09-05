#!/bin/bash
# This script will build JAR files, then create respective docker images and then push them to hub

echo "Current directory: $(pwd)"

# Remove the default builder and create a new one for multiplatform build
docker buildx rm default
docker buildx create --name multiplatform --driver docker-container --use
docker buildx ls
docker buildx use multiplatform # Switch to the new builder

# Build the frontend
cd ./frontend
npm run build
cd ..

# Copy the dist folder content to the static folder
cp -r ./frontend/dist/* ./backend/main-application/src/main/resources/static/

# Build main-application
cd ./backend/main-application
mvn clean install && mvn clean package
docker build -f dev.dockerfile -t main-application:v0.0.1 .
docker buildx build --platform linux/amd64,linux/arm64 -f dev.dockerfile -t niharikadutta/main-application:v0.0.1 --push .

# Build processor-service
cd ../processor-service
mvn clean install && mvn clean package
docker build -f dev.dockerfile -t processor-service:v0.0.1 .
docker buildx build --platform linux/amd64,linux/arm64 -f dev.dockerfile -t niharikadutta/processor-service:v0.0.1 --push .

# Build email-service
cd ../email-service
mvn clean install && mvn clean package
docker build -f dev.dockerfile -t email-service:v0.0.1 .
docker buildx build --platform linux/amd64,linux/arm64 -f dev.dockerfile -t niharikadutta/email-service:v0.0.1 --push .

echo "\n Deployment complete!"


# mvn clean install && mvn clean package
# docker build -f dev.dockerfile -t main-application:v0.0.1 .
# docker build -f dev.dockerfile -t processor-service:v0.0.1 .
# docker build -f dev.dockerfile -t email-service:v0.0.1 .

# build for both architectures (without pushing)
# docker buildx build --platform linux/amd64,linux/arm64 -f dev.dockerfile -t niharikadutta/main-application:v0.0.1 --push .

# docker run -p 4040:4040 main-application:v0.0.1

# Push to Docker Hub:
# docker push niharikadutta/main-application:v0.0.1
# docker push niharikadutta/processor-service:v0.0.1
# docker push niharikadutta/email-service:v0.0.1

# Pull images from Docker Hub
# docker pull niharikadutta/main-application:v0.0.1
# docker pull niharikadutta/processor-service:v0.0.1
# docker pull niharikadutta/email-service:v0.0.1
