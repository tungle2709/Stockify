#!/bin/bash

echo "🚀 Final Stockify Deployment"

# Build
mvn clean package -DskipTests -q
echo "✅ Build complete"

# Deploy
scp -i ~/.ssh/stockify-1762896980.pem -o StrictHostKeyChecking=no target/*.jar ec2-user@44.198.177.164:~/stockify.jar
echo "✅ JAR uploaded"

# Restart with proper Java path
ssh -i ~/.ssh/stockify-1762896980.pem -o StrictHostKeyChecking=no ec2-user@44.198.177.164 "
    pkill -f java 2>/dev/null
    sleep 3
    export JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto.x86_64
    export PATH=\$JAVA_HOME/bin:\$PATH
    nohup java -jar stockify.jar > app.log 2>&1 &
    echo 'Application started'
"

echo "✅ Application deployed and running"
echo ""
echo "🌐 Application URLs:"
echo "   Home: http://44.198.177.164:5000/"
echo "   Stocks: http://44.198.177.164:5000/stocks"
echo "   Portfolio: http://44.198.177.164:5000/portfolio"
echo "   Trading: http://44.198.177.164:5000/trading"
echo ""
echo "✅ Stockify is running properly!"
