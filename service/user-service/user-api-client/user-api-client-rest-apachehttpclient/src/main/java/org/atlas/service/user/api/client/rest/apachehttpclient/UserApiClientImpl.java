package org.atlas.service.user.api.client.rest.apachehttpclient;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.client.rest.apachehttpclient.HttpClientService;
import org.atlas.service.user.api.client.rest.model.ListUserResponse;
import org.atlas.service.user.api.client.UserApiClient;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiClientImpl implements UserApiClient {

  @Value("${app.api-client.rest.user-service.base-url:http://localhost:8081}")
  private String baseUrl;

  private final HttpClientService service;

  @Override
  public ListUserUseCase.Output call(ListUserUseCase.Input input) {
    String url = String.format("%s/api/internal/users", baseUrl);

    ListUserResponse response = service.doPost(url, null, input, ListUserResponse.class);
    return response.getData();
  }
}
