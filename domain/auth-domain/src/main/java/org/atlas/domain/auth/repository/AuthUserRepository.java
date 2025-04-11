package org.atlas.domain.auth.repository;

import java.util.Optional;
import org.atlas.domain.auth.entity.AuthUserEntity;

public interface AuthUserRepository {

  Optional<AuthUserEntity> findByIdentifier(String identifier);

  void insert(AuthUserEntity authUserEntity);
}