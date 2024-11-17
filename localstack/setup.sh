#!/bin/bash

# Set environment variables for AWS CLI to interact with LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=eu-west-1

# Define variables
FUNCTION_NAME="TheOfficeTriviaFunction"
ZIP_FILE="/var/task/lambda_function.zip"
ROLE_ARN="arn:aws:iam::000000000000:role/lambda-role"  # Dummy ARN for LocalStack
API_NAME="TheOfficeTriviaAPI"
RESOURCE_PATH="trivia"
STAGE_NAME="test"

# Create the S3 bucket
aws s3 mb s3://dunder-mifflin-bucket --endpoint-url=http://localhost:4566

# Create the Lambda function
aws lambda create-function \
    --function-name ${FUNCTION_NAME} \
    --runtime python3.8 \
    --handler lambda_function.lambda_handler \
    --zip-file fileb://${ZIP_FILE} \
    --role ${ROLE_ARN} \
    --endpoint-url=http://localhost:4566

# Get the function ARN (LocalStack uses a static ARN format)
FUNCTION_ARN="arn:aws:lambda:${AWS_DEFAULT_REGION}:000000000000:function:${FUNCTION_NAME}"

# Create the API Gateway REST API
API_ID=$(aws apigateway create-rest-api \
    --name "${API_NAME}" \
    --query 'id' \
    --output text \
    --endpoint-url=http://localhost:4566)

# Get the root resource ID
PARENT_ID=$(aws apigateway get-resources \
    --rest-api-id ${API_ID} \
    --query 'items[?path==`"/"`].id' \
    --output text \
    --endpoint-url=http://localhost:4566)

# Create a new resource under the root resource
RESOURCE_ID=$(aws apigateway create-resource \
    --rest-api-id ${API_ID} \
    --parent-id ${PARENT_ID} \
    --path-part ${RESOURCE_PATH} \
    --query 'id' \
    --output text \
    --endpoint-url=http://localhost:4566)

# Create a GET method on the new resource
aws apigateway put-method \
    --rest-api-id ${API_ID} \
    --resource-id ${RESOURCE_ID} \
    --http-method GET \
    --authorization-type "NONE" \
    --endpoint-url=http://localhost:4566

# Integrate the GET method with the Lambda function
aws apigateway put-integration \
    --rest-api-id ${API_ID} \
    --resource-id ${RESOURCE_ID} \
    --http-method GET \
    --type AWS_PROXY \
    --integration-http-method POST \
    --uri "arn:aws:apigateway:${AWS_DEFAULT_REGION}:lambda:path/2015-03-31/functions/${FUNCTION_ARN}/invocations" \
    --endpoint-url=http://localhost:4566

# Grant permission for API Gateway to invoke the Lambda function
aws lambda add-permission \
    --function-name ${FUNCTION_NAME} \
    --statement-id apigateway-invoke-permissions \
    --action lambda:InvokeFunction \
    --principal apigateway.amazonaws.com \
    --source-arn "arn:aws:execute-api:${AWS_DEFAULT_REGION}:000000000000:${API_ID}/*/GET/${RESOURCE_PATH}" \
    --endpoint-url=http://localhost:4566

# Deploy the API
aws apigateway create-deployment \
    --rest-api-id ${API_ID} \
    --stage-name ${STAGE_NAME} \
    --endpoint-url=http://localhost:4566

# Output the API endpoint
API_URL="http://localhost:4566/restapis/${API_ID}/${STAGE_NAME}/_user_request_/${RESOURCE_PATH}"
echo "API endpoint: ${API_URL}"
