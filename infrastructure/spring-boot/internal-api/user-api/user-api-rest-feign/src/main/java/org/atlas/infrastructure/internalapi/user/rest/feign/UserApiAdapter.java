package org.atlas.infrastructure.internalapi.user.rest.feign;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.shared.internal.ListUserInput;
import org.atlas.domain.user.shared.internal.ListUserOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.internalapi.UserApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.internalapi.user.rest.model.ListUserRequest;
import org.atlas.infrastructure.internalapi.user.rest.model.ListUserResponse;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default")
@CircuitBreaker(name = "default")
@Bulkhead(name = "default")
@RequiredArgsConstructor
public class UserApiAdapter implements UserApiPort {

  private final UserFeignClient feignClient;

  @Override
  public ListUserOutput call(ListUserInput input) {
    ListUserRequest request = ObjectMapperUtil.getInstance()
        .map(input, ListUserRequest.class);
    ApiResponseWrapper<ListUserResponse> apiResponseWrapper = feignClient.listUser(request);
    ListUserResponse responseData = apiResponseWrapper.getData();
    return ObjectMapperUtil.getInstance()
        .map(responseData, ListUserOutput.class);
  }
}
