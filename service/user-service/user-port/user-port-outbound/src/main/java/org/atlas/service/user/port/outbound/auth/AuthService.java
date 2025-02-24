package org.atlas.service.user.port.outbound.auth;

import org.atlas.service.user.domain.entity.UserEntity;

public interface AuthService {

  void register(UserEntity userEntity);

  LoginResponse login(LoginRequest request);

  void logout(String accessToken);
}
