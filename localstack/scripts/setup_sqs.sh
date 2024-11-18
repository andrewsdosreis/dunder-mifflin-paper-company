#!/bin/bash
# Create the SQS queue
QUEUE_NAME="quotes"
aws sqs create-queue --queue-name ${QUEUE_NAME} --endpoint-url=http://localhost:4566