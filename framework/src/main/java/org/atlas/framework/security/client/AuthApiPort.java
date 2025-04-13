package org.atlas.framework.security.client;

import org.atlas.framework.security.client.model.CreateUserRequest;

public interface AuthApiPort {

  void createUser(CreateUserRequest request);
}
