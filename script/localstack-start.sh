#!/bin/bash

PROJECT_NAME="atlas"

docker-compose -f deployment/aws/localstack/localstack.yml -p "$PROJECT_NAME" up -d
