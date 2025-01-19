# System Design

## Twelve-factor applications

The twelve-factor app is a collection of patterns for cloud-native application architectures, originally developed by engineers at Heroku. The patterns describe an application archetype that optimizes
for the “why” of cloud-native application architectures. They focus on speed, safety, and scale by emphasizing declarative configuration, stateless/shared-nothing processes that horizontally scale, and an overall loose coupling to the deployment environment. Cloud application platforms like Cloud Foundry, Heroku, and Amazon Elastic Beanstalk are optimized for deploying twelve-factor apps.

A twelve-factor app can be described in the following ways:

1. **Codebase**: Each deployable app is tracked as one codebase tracked in revision control. It may have many deployed instances across multiple environments.
2. **Dependencies**: An app explicitly declares and isolates dependencies via appropriate tooling (e.g., Maven, Bundler, NPM) rather than depending on implicitly realized dependencies in its deployment environment.
3. **Config**: Configuration, or anything that is likely to differ between deployment environments (e.g., development, staging, production) is injected via operating system-level environment variables.
4. **Backing services**: Backing services, such as databases or message brokers, are treated as attached resources and consumed identically across all environments.
5. **Build, release, run**: The stages of building a deployable app artifact, combining that artifact with configuration, and starting one or more processes from that artifact/configuration combination, are strictly separated.
6. **Processes**: The app executes as one or more stateless processes (e.g., master/workers) that share nothing. Any necessary state is externalized to backing services (cache, object store, etc.).
7. **Port binding**: The app is self-contained and exports any/all services via port binding (including HTTP).
8. **Concurrency**: Concurrency is usually accomplished by scaling out app processes horizontally (though processes may also multiplex work via internally managed threads if desired)
9. **Disposability**: Robustness is maximized via processes that start up quickly and shut down gracefully. These aspects allow for rapid elastic scaling, deployment of changes, and recovery from crashes.
10. **Dev/prod parity**: Continuous delivery and deployment are enabled by keeping development, staging, and production environments as similar as possible.
11. **Logs**: Rather than managing logfiles, treat logs as event streams, allowing the execution environment to collect, aggregate, index,  and analyze the events via centralized services.
12. **Admin processes**: Administrative or managements tasks, such as database migrations, are executed as one-off processes in environments identical to the app’s long-running processes.

---

## Service communication

### Synchronous

### Asynchronous

---

## Service discovery

### Client-side discovery

In the client-side discovery pattern, each microservice will communicate with the discovery service directly. In this pattern, the service gets the network location of all other services and their instance location from the discovery service before making the request to the required service. After getting the locations, the calling service is supposed to have smartness in choosing the proper instance of the required service by implementing some load balancing algorithms. Netflix Eureka is a good example of using this pattern. Ribbon is another tool that works with Eureka and helps the service to efficiently choose an instance for load balancing the request.

The client-side discovery pattern is simple and easy to implement with so many available tools. As in self-registration, there is a violation of the microservice's basic single responsibility rule. Now, each service is also responsible for finding the location of other microservices in the network, and also with some algorithm, it should make a request to a particular service instance to balance the traffic load. Still, organization uses this pattern for ease of use.

### Server-side discovery

In the server-side discovery pattern, services will make a request to a single middleware entity, such as a load balancer or gateway. This gateway will communicate with the discovery service to locate the required service, and use logic to find the exact instance to talk with. The API gateway is supposed to have the logic of managing the traffic load and choosing the best instance of that particular service. It is aligned with a single responsibility rule for microservices. Microservices can focus on their business logic itself.

---

## API gateway

---

## Event-driven architecture

https://blog.bytebytego.com/p/event-driven-architectural-patterns

### CQRS

---

## Fault-Tolerance

###  Circuit breakers

Circuit breakers insulate a service from its dependencies by preventing remote calls when a dependency is determined to be unhealthy, just as electrical circuit breakers protect homes from burning down due to excessive use of power. Circuit breakers  are implemented as state machines. When in their closed state, calls are simply passed through to the dependency. If any of these calls fails, the failure is counted. When the failure count reaches a specified threshold within a specified time period, the circuit trips into the open state. In the open state, calls always fail immediately. After a predetermined period of time, the circuit transitions into a “half-open” state. In this state, calls are again attempted to the remote dependency. Successful calls transition the circuit breaker back into the closed state, while failed calls return the circuit breaker to the open state.

### Bulkheads

---

## Externalize configuration

---

## Security

https://medium.com/swlh/all-you-need-to-know-about-authentication-is-here-25c8d8135cd6

---

## Observability

### Tracing

https://netflixtechblog.com/building-netflixs-distributed-tracing-infrastructure-bb856c319304

---

## Microservices Chassis

![](https://img.shields.io/badge/TODO-gray)

---

## Use cases

https://medium.com/walmartglobaltech/building-a-24-7-365-walmart-scale-java-application-12cb7e58df9c
