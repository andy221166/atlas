package org.atlas.edge.auth.springsecurityjwt.service;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.auth.entity.AuthUserEntity;
import org.atlas.domain.auth.repository.AuthUserRepository;
import org.atlas.edge.auth.springsecurityjwt.model.CreateUserRequest;
import org.atlas.framework.security.cryptography.PasswordUtil;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final AuthUserRepository authUserRepository;

  @Transactional
  public void createUser(CreateUserRequest request) {
    AuthUserEntity authUserEntity = ObjectMapperUtil.getInstance()
        .map(request, AuthUserEntity.class);
    authUserEntity.setPassword(PasswordUtil.hashPassword(request.getPassword()));
    authUserRepository.insert(authUserEntity);
  }
}
