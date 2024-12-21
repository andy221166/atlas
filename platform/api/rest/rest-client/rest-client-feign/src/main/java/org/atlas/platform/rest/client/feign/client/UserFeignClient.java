package org.atlas.platform.rest.client.feign.client;

import java.util.List;
import org.atlas.platform.rest.client.contract.user.ListUserResponse;
import org.atlas.platform.rest.client.feign.core.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "user-service",
    url = "${app.rest-client.user.base-url:http://localhost:8081}",
    configuration = FeignConfig.class
)
public interface UserFeignClient {

  @GetMapping("/api/users")
  ListUserResponse listUser(@RequestParam("ids") List<Integer> ids);
}
