#!/bin/bash
DB_INSTANCE_IDENTIFIER="dunder-mifflin-db"
DB_INSTANCE_CLASS="db.t3.large"
ENGINE="aurora-postgresql"
MASTER_USERNAME="test"
MASTER_USER_PASSWORD="test"
DB_NAME="dundermifflin"
SQL_INIT_SCRIPT_PATH="/etc/localstack/scripts/database/db-init.sql"

## Create the RDS instance
awslocal rds create-db-instance \
    --db-instance-identifier ${DB_INSTANCE_IDENTIFIER} \
    --engine ${ENGINE} \
    --db-instance-class ${DB_INSTANCE_CLASS} \
    --master-username ${MASTER_USERNAME} \
    --master-user-password ${MASTER_USER_PASSWORD} \
    --db-name ${DB_NAME} \
    --endpoint-url http://localhost:4566

## Wait for the DB instance to become available
awslocal rds wait db-instance-available \
    --db-instance-identifier ${DB_INSTANCE_IDENTIFIER} \
    --endpoint-url http://localhost:4566

## Get the DB endpoint
DB_ENDPOINT=$(awslocal rds describe-db-instances \
    --db-instance-identifier ${DB_INSTANCE_IDENTIFIER} \
    --query "DBInstances[0].Endpoint.Address" \
    --output text \
    --endpoint-url http://localhost:4566)

## Run the SQL initialization script using psql
echo "PGPASSWORD=${MASTER_USER_PASSWORD} psql -h ${DB_ENDPOINT} -p 4510 -U ${MASTER_USERNAME} -d ${DB_NAME} -f ${SQL_INIT_SCRIPT_PATH}"

#PGPASSWORD=${MASTER_USER_PASSWORD} psql -h ${DB_ENDPOINT} -p 4510 -U ${MASTER_USERNAME} -d ${DB_NAME} -f ${SQL_INIT_SCRIPT_PATH}