package org.atlas.infrastructure.auth.client.springsecurityjwt;

import org.atlas.framework.security.client.model.CreateUserRequest;
import org.atlas.infrastructure.api.client.core.rest.feign.FeignConfig;
import org.atlas.infrastructure.api.client.core.rest.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "spring-security-jwt",
    url = "${app.auth-client.spring-security-jwt.base-url:http://localhost:8091}",
    configuration = FeignConfig.class
)
public interface AuthFeignClient {

  @PostMapping("/api/auth/register")
  Response<Void> register(@RequestBody CreateUserRequest request);
}
