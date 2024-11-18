#!/bin/bash
# Create the elasticache Redis cluster
CACHE_NAME="dunder-mifflin-cache"
awslocal elasticache create-cache-cluster \
  --cache-cluster-id ${CACHE_NAME} \
  --cache-node-type cache.t2.micro \
  --engine redis \
  --num-cache-nodes 1