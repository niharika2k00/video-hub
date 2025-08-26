#!/bin/bash

# goto backend folder
# create JAR files and then build the docker images
# push the docker images to hub


cd /Users/niharikadutta/Workspace/projects/video-hub/backend/

# Build main-application
cd main-application
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







# chmod +x deploy.sh
# ./deploy.sh
