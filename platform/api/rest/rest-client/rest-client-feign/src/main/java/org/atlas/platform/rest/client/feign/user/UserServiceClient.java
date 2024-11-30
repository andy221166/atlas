package org.atlas.platform.rest.client.feign.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.client.contract.user.IUserServiceClient;
import org.atlas.platform.rest.client.contract.user.ListUserResponse;
import org.atlas.platform.rest.client.feign.client.UserFeignClient;
import org.atlas.service.user.contract.model.UserDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient implements IUserServiceClient {

  private final UserFeignClient feignClient;

  @Override
  public List<UserDto> listUser(List<Integer> ids) {
    ListUserResponse response = feignClient.listUser(ids);
    return response.getData();
  }
}
