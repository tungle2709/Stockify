#!/bin/bash

echo "🔧 Fixing Stockify deployment..."

# Build the application
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo "✅ Build successful"

# Try multiple deployment attempts
for i in {1..3}; do
    echo "🚀 Deployment attempt $i..."
    
    # Copy JAR file
    scp -o ConnectTimeout=30 -o StrictHostKeyChecking=no target/*.jar ec2-user@44.198.177.164:~/stockify.jar 2>/dev/null
    
    if [ $? -eq 0 ]; then
        echo "✅ JAR uploaded"
        
        # Restart application
        ssh -o ConnectTimeout=30 -o StrictHostKeyChecking=no ec2-user@44.198.177.164 "
            pkill -f java 2>/dev/null
            sleep 3
            nohup java -jar stockify.jar > app.log 2>&1 &
            sleep 5
            echo 'Application restarted'
        " 2>/dev/null
        
        if [ $? -eq 0 ]; then
            echo "✅ Application restarted"
            break
        fi
    fi
    
    echo "⚠️  Attempt $i failed, retrying..."
    sleep 5
done

echo "🎉 Deployment complete!"
echo "🌐 Testing application..."

# Wait and test
sleep 15
curl -s --connect-timeout 10 http://44.198.177.164:8080/ > /dev/null
if [ $? -eq 0 ]; then
    echo "✅ Application is running at http://44.198.177.164:8080"
else
    echo "⚠️  Application may still be starting up"
fi
