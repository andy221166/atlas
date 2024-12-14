#!/bin/bash

# Ensures that the script exits immediately if any command fails, or if you try to use an undefined variable.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/logger.sh" || { echo "Error: logger.sh could not be sourced."; exit 1; }

# Endpoint URL for LocalStack
LOCALSTACK_ENDPOINT="http://localhost:4566"

# Create SNS Topic
SNS_TOPIC_NAME="order_events"
SNS_TOPIC_ARN=$(aws --endpoint-url=$LOCALSTACK_ENDPOINT sns create-topic --name $SNS_TOPIC_NAME --query 'TopicArn' --output text)
log "Created SNS Topic: $SNS_TOPIC_ARN"

# List of Queues
QUEUES=(
  "order_events_order"
  "order_events_product"
  "order_events_user"
  "order_events_notification"
  "order_events_report"
)

# Iterate through the QUEUES array
for SQS_QUEUE_NAME in "${QUEUES[@]}"; do
  # Create SQS Queue
  SQS_QUEUE_URL=$(aws --endpoint-url=$LOCALSTACK_ENDPOINT sqs create-queue --queue-name $SQS_QUEUE_NAME --query 'QueueUrl' --output text)
  log "Created SQS Queue: $SQS_QUEUE_URL"

  # Get the Queue ARN
  SQS_QUEUE_ARN=$(aws --endpoint-url=$LOCALSTACK_ENDPOINT sqs get-queue-attributes \
      --queue-url $SQS_QUEUE_URL \
      --attribute-names QueueArn \
      --query 'Attributes.QueueArn' --output text)
  log "SQS Queue ARN: $SQS_QUEUE_ARN"

  # Subscribe the SQS Queue to the SNS Topic
  SUBSCRIPTION_ARN=$(aws --endpoint-url=$LOCALSTACK_ENDPOINT sns subscribe \
      --topic-arn $SNS_TOPIC_ARN \
      --protocol sqs \
      --notification-endpoint $SQS_QUEUE_ARN \
      --attributes RawMessageDelivery=true \
      --query 'SubscriptionArn' --output text)
  log "Subscribed SQS Queue '$SQS_QUEUE_NAME' to SNS Topic. Subscription ARN: $SUBSCRIPTION_ARN"
done
