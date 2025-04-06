package org.atlas.infrastructure.internalapi.user.rest.feign;

import org.atlas.infrastructure.api.client.core.rest.feign.FeignConfig;
import org.atlas.infrastructure.api.client.core.rest.model.Response;
import org.atlas.infrastructure.internalapi.user.rest.model.ListUserRequest;
import org.atlas.infrastructure.internalapi.user.rest.model.ListUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "user-service",
    url = "${app.api-client.rest.user-service.base-url:http://localhost:8081}",
    configuration = FeignConfig.class
)
public interface UserFeignClient {

  @PostMapping("/api/internal/users/list")
  Response<ListUserResponse> listUser(@RequestBody ListUserRequest input);
}
