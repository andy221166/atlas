package org.atlas.service.user.api.client.rest.feign;

import org.atlas.platform.api.client.rest.feign.FeignConfig;
import org.atlas.service.user.api.client.rest.model.ListUserResponse;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "user-service",
    url = "${app.api-client.rest.user-service.base-url:http://localhost:8081}",
    configuration = FeignConfig.class
)
public interface UserFeignClient {

  @PostMapping("/api/internal/users")
  ListUserResponse listUser(@RequestBody ListUserUseCase.Input input);
}
