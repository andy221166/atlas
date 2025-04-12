# Messaging

## Apache Kafka

### Error handling

#### Error handling - Producer side

1. Enable Idempotence (Recommended)

To avoid duplicates during retries, enable idempotence:

```properties
enable.idempotence=true
```

- Ensures exactly-once delivery semantics per partition.
- Automatically sets:
  - `acks=all`
  - `retries=Integer.MAX_VALUE`
  - `max.in.flight.requests.per.connection <= 5`

2. Configure Retries and Backoff

If a transient failure occurs (e.g., broker is temporarily unavailable), Kafka can retry automatically.

```properties
retries=5 # Number of times Kafka will retry sending the record.
retry.backoff.ms=1000 ## Delay between retries.
```

Without idempotence, retries may cause duplicate messages if the broker receives the message but the acknowledgment is lost.

3. Use Proper Acknowledgment Level

```properties
acks=all
```

- `acks=0`: Fire and forget (no durability).
- `acks=1`: Leader only ack (risk of data loss if leader fails).
- `acks=all`: All replicas must ack — safest but may increase latency.

4. Handle Exceptions in Code

Always wrap your `send()` calls with exception handling:

✅ Example (Synchronous Send):

```java
try {
  producer.send(record).get(); // wait for ack
} catch (ExecutionException | InterruptedException e) {
  // Log or retry based on the exception type
  e.printStackTrace();
}
```

✅ Example (Asynchronous Send with Callback):

```java
producer.send(record, (metadata, exception) -> {
    if (exception != null) {
        // Handle failure
        exception.printStackTrace();
    } else {
        // Success
        System.out.printf("Produced to topic %s, partition %d%n", metadata.topic(), metadata.partition());
    }
});
```

5. Handle Serialization Failures

Set a custom serializer or use error handling with Spring Kafka.

If using Spring Kafka:

```properties
key.serializer=org.springframework.kafka.support.serializer.ErrorHandlingSerializer
value.serializer=org.springframework.kafka.support.serializer.ErrorHandlingSerializer
```

Then wrap your actual serializer inside this.

6. Implement a Dead Letter Queue (Optional)

If a message fails to send even after retries (e.g., due to serialization or business logic errors), you can push it to a DLQ topic for offline processing.

You need to manually handle this in the producer logic:

```java
if (failedRecord != null) {
    producer.send(new ProducerRecord<>("dead-letter-topic", failedRecord));
}
```

7. Tune Buffering and Timeouts

Useful for performance and graceful error handling:

```properties
linger.ms=10               # Wait before batching
batch.size=16384           # Batch size
buffer.memory=33554432     # Total buffer size
max.block.ms=60000         # Time producer will block if buffer is full
```

If `max.block.ms` is too small and the buffer is full, you'll get a `TimeoutException`.

#### Error handling - Consumer side
