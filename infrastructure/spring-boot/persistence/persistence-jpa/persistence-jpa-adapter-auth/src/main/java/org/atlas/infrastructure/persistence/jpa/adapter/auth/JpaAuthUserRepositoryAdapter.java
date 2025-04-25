package org.atlas.infrastructure.persistence.jpa.adapter.auth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.auth.entity.AuthUserEntity;
import org.atlas.domain.auth.repository.AuthUserRepository;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.persistence.jpa.adapter.auth.entity.JpaAuthUserEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.auth.repository.JpaAuthUserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaAuthUserRepositoryAdapter implements AuthUserRepository {

  private final JpaAuthUserRepository jpaAuthUserRepository;

  @Override
  public Optional<AuthUserEntity> findByIdentifier(String identifier) {
    return jpaAuthUserRepository.findByIdentifier(identifier)
        .map(jpaAuthUserEntity -> ObjectMapperUtil.getInstance()
            .map(jpaAuthUserEntity, AuthUserEntity.class));
  }

  @Override
  public void insert(AuthUserEntity authUserEntity) {
    JpaAuthUserEntity jpaAuthUserEntity = ObjectMapperUtil.getInstance()
        .map(authUserEntity, JpaAuthUserEntity.class);
    jpaAuthUserRepository.save(jpaAuthUserEntity);
  }
}
