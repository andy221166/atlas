package org.atlas.service.user.port.outbound.auth;

import org.atlas.service.user.domain.entity.UserEntity;

public interface AuthPort {

  void register(UserEntity userEntity);

  LoginResponse login(LoginRequest request);

  void logout(String accessToken);
}
