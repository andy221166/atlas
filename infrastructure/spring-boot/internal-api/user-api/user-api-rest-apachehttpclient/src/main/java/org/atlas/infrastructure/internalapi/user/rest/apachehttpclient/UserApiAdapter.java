package org.atlas.infrastructure.internalapi.user.rest.apachehttpclient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.shared.internal.ListUserInput;
import org.atlas.domain.user.shared.internal.ListUserOutput;
import org.atlas.framework.internalapi.UserApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.client.core.rest.apachehttpclient.HttpClientService;
import org.atlas.infrastructure.api.client.core.rest.model.Response;
import org.atlas.infrastructure.internalapi.user.rest.model.ListUserRequest;
import org.atlas.infrastructure.internalapi.user.rest.model.ListUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal-api")
@CircuitBreaker(name = "default-internal-api")
@Bulkhead(name = "default-internal-api")
@RequiredArgsConstructor
public class UserApiAdapter implements UserApiPort {

  @Value("${app.api-client.rest.user-service.base-url:http://localhost:8081}")
  private String baseUrl;

  private final HttpClientService service;

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
