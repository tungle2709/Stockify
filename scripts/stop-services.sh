#!/bin/bash

# Stop Stockify services
echo "Stopping RDS instance..."
aws rds stop-db-instance --db-instance-identifier stockify-db-small --region us-east-1

echo "Services stopping... This will save ~$12/month when not in use"
