package org.atlas.platform.api.client.user.rest.feign;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.api.client.user.rest.model.ListUserResponse;
import org.atlas.service.user.port.inbound.usecase.internal.ListUserUseCase;
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
