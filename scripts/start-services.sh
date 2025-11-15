#!/bin/bash

# Start Stockify services
echo "Starting RDS instance..."
aws rds start-db-instance --db-instance-identifier stockify-db-small --region us-east-1

echo "Services starting... Check status in AWS console"
echo "New RDS endpoint will be: stockify-db-small.cgz8egkws9y8.us-east-1.rds.amazonaws.com"
