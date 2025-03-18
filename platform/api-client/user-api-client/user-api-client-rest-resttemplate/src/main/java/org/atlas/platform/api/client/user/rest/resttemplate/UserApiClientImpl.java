package org.atlas.platform.api.client.user.rest.resttemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.rest.resttemplate.RestTemplateService;
import org.atlas.platform.api.client.user.UserApiClient;
import org.atlas.platform.api.client.user.rest.model.ListUserResponse;
import org.atlas.service.user.port.inbound.usecase.internal.ListUserUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserApiClientImpl implements UserApiClient {

  @Value("${app.api-client.rest.user-service.base-url:http://localhost:8081}")
  private String baseUrl;

  private final RestTemplateService service;

  @Override
  public ListUserUseCase.Output call(ListUserUseCase.Input input) {
    String url = String.format("%s/api/internal/users/list", baseUrl);

    ListUserResponse response =
        service.doPost(url, null, input, ListUserResponse.class);
    return response.getData();
  }
}
