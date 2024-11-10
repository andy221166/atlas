package org.atlas.platform.api.client.contract.user;

import org.atlas.service.user.contract.model.UserDto;

import java.util.List;

public interface IUserServiceClient {

    List<UserDto> listUser(List<Integer> ids);
}
