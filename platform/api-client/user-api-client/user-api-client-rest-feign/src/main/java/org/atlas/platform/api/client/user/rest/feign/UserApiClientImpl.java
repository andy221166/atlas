package org.atlas.platform.api.client.user.rest.feign;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.rest.model.Response;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.api.client.user.rest.model.ListUserRequest;
import org.atlas.platform.api.client.user.rest.model.ListUserResponse;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiClientImpl implements UserApiClient {

  private final UserFeignClient feignClient;

  @Override
  public ListUserOutput call(ListUserInput input) {
    ListUserRequest request = ObjectMapperUtil.getInstance()
        .map(input, ListUserRequest.class);
    Response<ListUserResponse> response = feignClient.listUser(request);
    ListUserResponse responseData = response.getData();
    return ObjectMapperUtil.getInstance()
        .map(responseData, ListUserOutput.class);
  }
}
