#!/bin/bash
set -e
set -x

# Load environment variables
source /etc/localstack/scripts/setup/env.sh

# Execute individual setup scripts
bash /etc/localstack/scripts/setup/setup_s3.sh
bash /etc/localstack/scripts/setup/setup_sqs.sh
bash /etc/localstack/scripts/setup/setup_rds.sh
bash /etc/localstack/scripts/setup/setup_redis.sh
bash /etc/localstack/scripts/setup/setup_lambda.sh
bash /etc/localstack/scripts/setup/setup_apigateway.sh