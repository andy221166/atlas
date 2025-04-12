# Data management

## Data concistency

https://levelup.gitconnected.com/system-design-concepts-data-consistency-%EF%B8%8F-a7a3a0870275

---

## Database Per Microservice Pattern

Problem:
A single database for all microservices creates bottlenecks and tight coupling.

Solution:
Each microservice has its own database, improving scalability and independence.

Best Practices:
✔ Use polyglot persistence (choose the best database for each service).
✔ Avoid cross-service joins — use APIs instead.

---

## Distributed transaction

### Saga pattern

A Saga is a sequence of local transactions where each transaction updates the database and publishes an event to trigger the next step in the workflow. If a failure occurs at any step, compensating transactions are executed to roll back the changes made by previous steps.

There are two primary ways to implement the Saga Pattern:

1. Choreography-Based Saga (Event-Driven)
   In this approach, each service listens to domain events from other services and executes its local transaction accordingly.
   Services communicate through an event broker (e.g., Kafka, RabbitMQ, or AWS SNS/SQS).
   No central orchestrator controls the workflow.
   ✔ Advantages:
    - Reduces coupling between services.
    - Easier to scale. 
    - Best for simple workflows. 
   ❌ Disadvantages:
    - Hard to manage complex workflows. 
    - Debugging and tracing failures can be difficult.
2. Orchestration-Based Saga (Centralized Controller)
   A central Saga Orchestrator (e.g., a dedicated service) coordinates the sequence of transactions.
   The orchestrator invokes services and listens for success or failure responses.
   It triggers compensating transactions if a failure occurs.
   ✔ Advantages:
   - Easier to implement complex workflows.
   - Better monitoring and debugging. 
   ❌ Disadvantages:
   - Introduces a single point of failure. 
   - Slightly higher coupling compared to choreography.

---

## Dual-writes

### Outbox pattern

Disadvantages:
- First, implementing the outbox pattern requires developers to design and maintain the outbox table, manage its cleanup, and ensure that asynchronous processing works reliably. Mistakes in handling the outbox, such as using overly complicated locking mechanisms or mismanaging the data flow, can introduce bugs and inefficiencies. 
- Additionally, the operational overhead is high, as the outbox pattern depends on additional processes to query the outbox table, publish messages, and then delete the entries. This can lead to increased resource consumption and latency compared to a direct database-to-queue approach.

### CDC

---

## Race-condition
