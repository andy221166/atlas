package org.atlas.platform.api.client.user.rest.resttemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.rest.model.Response;
import org.atlas.platform.api.client.rest.resttemplate.RestTemplateService;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.api.client.user.rest.model.ListUserRequest;
import org.atlas.platform.api.client.user.rest.model.ListUserResponse;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal")
@CircuitBreaker(name = "default-internal")
@Bulkhead(name = "default-internal")
@RequiredArgsConstructor
public class UserApiClientImpl implements UserApiClient {

  @Value("${app.api-client.rest.user-service.base-url:http://localhost:8081}")
  private String baseUrl;

  private final RestTemplateService service;

  @Override
  @SuppressWarnings("unchecked")
  public ListUserOutput call(ListUserInput input) {
    String url = String.format("%s/api/internal/users/list", baseUrl);
    ListUserRequest request = ObjectMapperUtil.getInstance()
        .map(input, ListUserRequest.class);
    Response<ListUserResponse> response =
        service.doPost(url, null, request, Response.class);
    ListUserResponse responseData = response.getData();
    return ObjectMapperUtil.getInstance()
        .map(responseData, ListUserOutput.class);
  }
}
