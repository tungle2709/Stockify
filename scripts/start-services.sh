#!/bin/bash

# Start Stockify services
echo "Starting EC2 instance..."
aws ec2 start-instances --instance-ids i-02e9d5e05f88ed315 --region us-east-1

echo "Starting RDS instance..."
aws rds start-db-instance --db-instance-identifier stockify-db --region us-east-1

echo "Services starting... Check status in AWS console"
