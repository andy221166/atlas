package org.atlas.platform.authprovider.core.service;

import org.atlas.platform.authprovider.core.model.LoginRequest;
import org.atlas.platform.authprovider.core.model.LoginResponse;
import org.atlas.platform.authprovider.core.model.LogoutRequest;
import org.atlas.platform.authprovider.core.model.RegisterRequest;

public interface AuthProvider {

  LoginResponse login(LoginRequest request);
  void register(RegisterRequest request);
  void logout(LogoutRequest request);
}
