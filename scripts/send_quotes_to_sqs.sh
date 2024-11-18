#!/bin/bash

# Define the queue URL (use the LocalStack endpoint)
QUEUE_URL="http://sqs.eu-west-1.localhost.localstack.cloud:4566/000000000000/quotes"

# Check if jq is installed
if ! command -v jq &> /dev/null
then
    echo "jq could not be found, please install it first"
    exit 1
fi

# Ensure LocalStack is running
#if ! curl -s http://localhost:4566/health | jq -e '.services.sqs'; then
#    echo "LocalStack SQS is not running. Please start LocalStack and try again."
#    exit 1
#fi

# Read the JSON file
messages=$(cat quotes.json)

# Check if the JSON file is valid
if ! echo "$messages" | jq empty; then
    echo "The JSON file is invalid."
    exit 1
fi

# Disable AWS CLI pager
export AWS_PAGER=""

# Iterate through each item and send messages to SQS
echo "$messages" | jq -c '.[]' | while read -r quote; do
    NAME=$(echo "$quote" | jq -r '.name')
    QUOTE=$(echo "$quote" | jq -r '.quote')

    # Create a combined message body
    MESSAGE_BODY=$(jq -n --arg name "$NAME" --arg quote "$QUOTE" '{name: $name, quote: $quote}')

    # Send message to SQS using LocalStack endpoint
    aws sqs send-message \
        --queue-url ${QUEUE_URL} \
        --message-body "${MESSAGE_BODY}" \
        --endpoint-url=http://localhost:4566

    if [ $? -eq 0 ]; then
        echo "Sent message from ${NAME}: ${QUOTE}"
    else
        echo "Failed to send message from ${NAME}"
    fi
done