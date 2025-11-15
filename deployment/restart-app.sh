#!/bin/bash

echo "Restarting Stockify Application..."

# Build the application
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi

echo "Build successful"

# Deploy to EC2
echo "Uploading JAR..."
scp -i ~/.ssh/stockify-1762896980.pem -o StrictHostKeyChecking=no target/*.jar ec2-user@44.198.177.164:~/stockify.jar

if [ $? -ne 0 ]; then
    echo "Upload failed"
    exit 1
fi

echo "JAR uploaded"

# Restart application with memory optimization
echo "Restarting application..."
ssh -i ~/.ssh/stockify-1762896980.pem -o StrictHostKeyChecking=no ec2-user@44.198.177.164 "
    pkill -f java 2>/dev/null
    sleep 5
    nohup /usr/lib/jvm/java-17-amazon-corretto.x86_64/bin/java -Xmx512m -Xms256m -jar stockify.jar > app.log 2>&1 &
    sleep 10
    echo 'Application restarted with memory optimization'
"

echo "Application restarted"
echo "Waiting for startup..."
sleep 15

# Test the application
curl -s --connect-timeout 10 http://44.198.177.164:5000/ > /dev/null
if [ $? -eq 0 ]; then
    echo "Application is running!"
    echo "Access at: http://44.198.177.164:5000"
else
    echo "Application may still be starting up"
fi
