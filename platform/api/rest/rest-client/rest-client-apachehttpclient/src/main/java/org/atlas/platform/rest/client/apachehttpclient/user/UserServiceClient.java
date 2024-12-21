package org.atlas.platform.rest.client.apachehttpclient.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.api.client.contract.user.IUserServiceClient;
import org.atlas.platform.rest.client.apachehttpclient.core.HttpClientService;
import org.atlas.platform.rest.client.contract.user.ListUserResponse;
import org.atlas.service.user.contract.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient implements IUserServiceClient {

  @Value("${app.rest-client.user.base-url:http://localhost:8081}")
  private String baseUrl;

  private final HttpClientService service;

  @Override
  public List<UserDto> listUser(List<Integer> ids) {
    String url = String.format("%s/api/users", baseUrl);

    Map<String, String> paramsMap = new HashMap<>();
    paramsMap.put("ids", StringUtils.join(ids, ","));

    ListUserResponse response = service.doGet(url, paramsMap, null, ListUserResponse.class);
    return response.getData();
  }
}
