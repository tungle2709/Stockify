#!/bin/bash

# Stop Stockify services
echo "Stopping EC2 instance..."
aws ec2 stop-instances --instance-ids i-02e9d5e05f88ed315 --region us-east-1

echo "Stopping RDS instance..."
aws rds stop-db-instance --db-instance-identifier stockify-db --region us-east-1

echo "Services stopping... This will save ~$20/month when not in use"
