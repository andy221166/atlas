package org.atlas.platform.api.client.contract.user;

import java.util.List;
import org.atlas.service.user.contract.model.UserDto;

public interface IUserServiceClient {

  List<UserDto> listUser(List<Integer> ids);
}
