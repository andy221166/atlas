package org.atlas.framework.auth;

import org.atlas.framework.auth.model.CreateUserRequest;

public interface AuthApiPort {

  void createUser(CreateUserRequest request);
}
