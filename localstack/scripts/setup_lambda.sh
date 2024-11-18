#!/bin/bash
# Create or update the Lambda function
FUNCTION_NAME="TheOfficeTriviaFunction"
ZIP_FILE="/var/task/trivia_api.zip"
ROLE_ARN="arn:aws:iam::000000000000:role/lambda-role" # Dummy ARN for LocalStack

aws lambda delete-function \
    --function-name ${FUNCTION_NAME} \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION} || true

aws lambda create-function \
    --function-name ${FUNCTION_NAME} \
    --runtime python3.8 \
    --handler trivia_api.lambda_handler \
    --zip-file fileb://${ZIP_FILE} \
    --role ${ROLE_ARN} \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION}