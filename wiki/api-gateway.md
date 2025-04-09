# API gateway

The API Gateway acts as a single entry point, routing requests to the appropriate microservices.

How It Works:
1. Client sends a request to the API Gateway. 
2. Gateway forwards the request to the right microservice(s).
3. Gateway aggregates responses if needed and returns a single response to the client.

Solution:
- [Spring Cloud Gateway](/edge/gateway-server/gateway-server-spring-cloud)

Checklist:
- [x] Routing
- [x] Authentication
- [x] CORS
- [ ] Rate limiting
- [ ] Aggregate response
- [x] API document
