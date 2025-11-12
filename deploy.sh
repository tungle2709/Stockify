#!/bin/bash

EC2_HOST="44.198.177.164"
KEY_PATH="stockify-1762896980.pem"

echo "🚀 Deploying original Stockify to EC2..."

mvn clean package -DskipTests

scp -i ~/.ssh/$KEY_PATH target/*.jar ec2-user@$EC2_HOST:~/

ssh -i ~/.ssh/$KEY_PATH ec2-user@$EC2_HOST << 'EOF'
sudo yum update -y
sudo yum install -y java-17-amazon-corretto
pkill -f java || true
nohup /usr/lib/jvm/java-17-amazon-corretto.x86_64/bin/java -jar *.jar > app.log 2>&1 &
echo "✅ Original Stockify deployed"
EOF

echo "🎉 Deployment complete!"
echo "🌐 Access your app at: http://$EC2_HOST:8080"
