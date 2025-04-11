package org.atlas.infrastructure.auth.client.springsecurityjwt;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.framework.security.AuthApiPort;
import org.atlas.framework.security.model.CreateUserRequest;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal-api")
@CircuitBreaker(name = "default-internal-api")
@Bulkhead(name = "default-internal-api")
@RequiredArgsConstructor
public class AuthApiAdapter implements AuthApiPort {

  private final AuthFeignClient feignClient;

  @Override
  public void createUser(CreateUserRequest request) {
    feignClient.register(request);
  }
}
