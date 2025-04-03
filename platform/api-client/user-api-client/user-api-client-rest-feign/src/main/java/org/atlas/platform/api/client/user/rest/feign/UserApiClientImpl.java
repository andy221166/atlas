package org.atlas.platform.api.client.user.rest.feign;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.rest.model.Response;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.api.client.user.rest.model.ListUserRequest;
import org.atlas.platform.api.client.user.rest.model.ListUserResponse;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal")
@CircuitBreaker(name = "default-internal")
@Bulkhead(name = "default-internal")
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
