#!/bin/bash
set -e
set -x

# Load environment variables
source /etc/localstack/scripts/env.sh

# Execute individual setup scripts
bash /etc/localstack/scripts/setup_s3.sh
bash /etc/localstack/scripts/setup_sqs.sh
bash /etc/localstack/scripts/setup_redis.sh
bash /etc/localstack/scripts/setup_lambda.sh
bash /etc/localstack/scripts/setup_apigateway.sh