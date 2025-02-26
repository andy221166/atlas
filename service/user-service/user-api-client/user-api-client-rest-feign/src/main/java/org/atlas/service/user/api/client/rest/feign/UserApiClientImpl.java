package org.atlas.service.user.api.client.rest.feign;

import lombok.RequiredArgsConstructor;
import org.atlas.service.user.api.client.UserApiClient;
import org.atlas.service.user.api.client.rest.model.ListUserResponse;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiClientImpl implements UserApiClient {

  private final UserFeignClient feignClient;

  @Override
  public ListUserUseCase.Output call(ListUserUseCase.Input input) {
    ListUserResponse response = feignClient.listUser(input);
    return response.getData();
  }
}
