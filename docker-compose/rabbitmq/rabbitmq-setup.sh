#!/bin/bash

ORDER_EXCHANGE="order"
QUEUES=(
  "order_created.product"
  "reserve_quantity_success.user"
  "reserve_quantity_failed.order"
  "reserve_credit_success.order"
  "reserve_credit_failed.order"
  "reserve_credit_failed.product"
  "order_confirmed.notification"
  "order_confirmed.report"
  "order_canceled.notification"
)

# Declare exchange
echo "Declaring exchange: ${ORDER_EXCHANGE}"
rabbitmqadmin declare exchange name=${ORDER_EXCHANGE} type=topic durable=true

# Declare queues and bindings
for queue in "${QUEUES[@]}"; do
  # Extract routing key by taking the part before the last dot
  ROUTING_KEY=$(echo "$queue" | sed 's/\(.*\)\.[^.]*$/\1/')

  echo "Declaring queue $queue with routing key $ROUTING_KEY"
  rabbitmqadmin declare queue name="$queue" durable=true
  rabbitmqadmin declare binding source="$ORDER_EXCHANGE" destination="$queue" routing_key="$ROUTING_KEY"
done
