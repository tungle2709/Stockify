#!/bin/bash

# Launch new EC2 instance from Stockify AMI
echo "Launching new EC2 instance from AMI..."

INSTANCE_ID=$(aws ec2 run-instances \
  --image-id ami-09c2b4b9d1f38f71a \
  --instance-type t3.micro \
  --key-name stockify-1762896980 \
  --security-group-ids sg-081bee41b0f9d8975 \
  --subnet-id subnet-028fc51ebe951f821 \
  --region us-east-1 \
  --query 'Instances[0].InstanceId' \
  --output text)

echo "New instance launched: $INSTANCE_ID"
echo "Wait 2-3 minutes for boot, then get public IP:"
echo "aws ec2 describe-instances --instance-ids $INSTANCE_ID --region us-east-1 --query 'Reservations[0].Instances[0].PublicIpAddress'"
