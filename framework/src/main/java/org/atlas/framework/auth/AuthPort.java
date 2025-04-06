package org.atlas.framework.auth;

import org.atlas.framework.auth.model.LoginRequest;
import org.atlas.framework.auth.model.LoginResponse;
import org.atlas.framework.auth.model.RegisterRequest;

public interface AuthPort {

  void register(RegisterRequest request);

  LoginResponse login(LoginRequest request);

  void logout(String accessToken);
}
