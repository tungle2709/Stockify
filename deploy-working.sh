#!/bin/bash

echo "Deploying Stockify with proper SSH key..."

# Build the application
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi

echo "Build successful"

# Deploy with correct SSH key
echo "Uploading JAR..."
scp -i ~/.ssh/stockify-1762896980.pem -o StrictHostKeyChecking=no target/*.jar ec2-user@44.198.177.164:~/stockify.jar

if [ $? -ne 0 ]; then
    echo "Upload failed"
    exit 1
fi

echo "JAR uploaded"

# Restart application
echo "Restarting application..."
ssh -i ~/.ssh/stockify-1762896980.pem -o StrictHostKeyChecking=no ec2-user@44.198.177.164 "
    pkill -f java 2>/dev/null
    sleep 3
    nohup java -jar stockify.jar > app.log 2>&1 &
    sleep 2
    echo 'Application started'
"

echo "Application restarted"
echo "Waiting for startup..."
sleep 20

# Test the application
curl -s --connect-timeout 10 http://44.198.177.164:8080/ > /dev/null
if [ $? -eq 0 ]; then
    echo "Application is running!"
    echo "Access at: http://44.198.177.164:8080"
    echo "Stocks page: http://44.198.177.164:8080/stocks"
else
    echo "Application may still be starting up"
fi
