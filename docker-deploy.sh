#!/bin/bash

echo "Building Stockify application..."
mvn clean package -DskipTests

echo "Building Docker image..."
docker build -t stockify:latest .

echo "Starting Docker containers..."
docker-compose up -d

echo "Waiting for application to start..."
sleep 30

echo "Docker deployment complete!"
echo "Access your app at: http://localhost:5000"
