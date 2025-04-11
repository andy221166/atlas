package org.atlas.framework.security;

import org.atlas.framework.security.model.CreateUserRequest;

public interface AuthApiPort {

  void createUser(CreateUserRequest request);
}
