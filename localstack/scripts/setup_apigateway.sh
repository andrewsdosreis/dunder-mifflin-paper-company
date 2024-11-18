#!/bin/bash
# Create or update API Gateway for the Lambda function
API_NAME="TheOfficeTriviaAPI"
RESOURCE_PATH="trivia"
STAGE_NAME="test"
AWS_DEFAULT_REGION=eu-west-1
FUNCTION_NAME="TheOfficeTriviaFunction"
FUNCTION_ARN="arn:aws:lambda:${AWS_DEFAULT_REGION}:000000000000:function:${FUNCTION_NAME}"

# Check if the API already exists
API_ID=$(aws apigateway get-rest-apis \
    --query "items[?name=='${API_NAME}'].id | [0]" \
    --output text \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION})

if [ "$API_ID" == "None" ] || [ -z "$API_ID" ]; then
    # Create the API Gateway REST API
    API_ID=$(aws apigateway create-rest-api \
        --name "${API_NAME}" \
        --query 'id' \
        --output text \
        --endpoint-url=http://localhost:4566 \
        --region ${AWS_DEFAULT_REGION})
    echo "Created REST API with ID: $API_ID"
else
    echo "Using existing REST API with ID: $API_ID"
fi

# Get the root resource ID
PARENT_ID=$(aws apigateway get-resources \
    --rest-api-id ${API_ID} \
    --query 'items[?path==`"/"`].id' \
    --output text \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION})

# Check if the resource already exists
RESOURCE_ID=$(aws apigateway get-resources \
    --rest-api-id ${API_ID} \
    --query "items[?pathPart=='${RESOURCE_PATH}'].id | [0]" \
    --output text \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION})

if [ "$RESOURCE_ID" == "None" ] || [ -z "$RESOURCE_ID" ]; then
    # Create a new resource under the root resource
    RESOURCE_ID=$(aws apigateway create-resource \
        --rest-api-id ${API_ID} \
        --parent-id ${PARENT_ID} \
        --path-part ${RESOURCE_PATH} \
        --query 'id' \
        --output text \
        --endpoint-url=http://localhost:4566 \
        --region ${AWS_DEFAULT_REGION})
    echo "Created resource with ID: $RESOURCE_ID"
else
    echo "Using existing resource with ID: $RESOURCE_ID"
fi

# Create a GET method on the resource (if not exists)
METHOD_EXISTS=$(aws apigateway get-method \
    --rest-api-id ${API_ID} \
    --resource-id ${RESOURCE_ID} \
    --http-method GET \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION} || echo "NotFound")

if [ "$METHOD_EXISTS" == "NotFound" ]; then
    aws apigateway put-method \
        --rest-api-id ${API_ID} \
        --resource-id ${RESOURCE_ID} \
        --http-method GET \
        --authorization-type "NONE" \
        --endpoint-url=http://localhost:4566 \
        --region ${AWS_DEFAULT_REGION}
fi

# Integrate the GET method with the Lambda function
aws apigateway put-integration \
    --rest-api-id ${API_ID} \
    --resource-id ${RESOURCE_ID} \
    --http-method GET \
    --type AWS_PROXY \
    --integration-http-method POST \
    --uri "arn:aws:apigateway:${AWS_DEFAULT_REGION}:lambda:path/2015-03-31/functions/${FUNCTION_ARN}/invocations" \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION}

# Grant permission for API Gateway to invoke the Lambda function
aws lambda add-permission \
    --function-name ${FUNCTION_NAME} \
    --statement-id apigateway-invoke-permissions \
    --action lambda:InvokeFunction \
    --principal apigateway.amazonaws.com \
    --source-arn "arn:aws:execute-api:${AWS_DEFAULT_REGION}:000000000000:${API_ID}/*/GET/${RESOURCE_PATH}" \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION} || true

# Deploy the API
aws apigateway create-deployment \
    --rest-api-id ${API_ID} \
    --stage-name ${STAGE_NAME} \
    --endpoint-url=http://localhost:4566 \
    --region ${AWS_DEFAULT_REGION}

# Output the API endpoint
API_URL="http://localhost:4566/restapis/${API_ID}/${STAGE_NAME}/_user_request_/${RESOURCE_PATH}"
echo "API endpoint: ${API_URL}"