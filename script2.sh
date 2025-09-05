#!/bin/bash

echo "=== AWS EC2 Instance A Setup and Deployment ==="

# System updates and installations
echo "Updating system packages..."
sudo apt update -y

echo "Installing Docker..."
sudo snap install docker

echo "Installing additional tools..."
sudo snap install aws-cli --classic
sudo apt install kafkacat -y
sudo apt install mysql-client -y
sudo apt install coreutils -y  # nohup is part of coreutils
sudo apt install tree -y

aws --version
nohup --version
kcat -V
ffmpeg -version
which ffprobe

# Docker permissions setup
echo "Setting up Docker permissions..."
ls -l /var/run/docker.sock
sudo chmod 666 /var/run/docker.sock

# Pull Docker images from Docker Hub
echo "Pulling Docker images..."
docker pull niharikadutta/main-application:v0.0.1
docker pull niharikadutta/processor-service:v0.0.1
docker pull niharikadutta/email-service:v0.0.1

echo "Current running containers:"
docker ps

# Start service for main java spring boot application
echo "Starting application service..."
docker compose up -d

echo "Current containers after compose up:"
docker ps

# Start MySQL service
echo "Starting MySQL service..."
cd ./deployment/docker-mysql
docker compose up -d

echo "Final container status:"
docker ps

echo "=== Setup Complete ==="
echo "To view application logs, run: docker logs -f main-application"
