package org.atlas.infrastructure.persistence.jpa.adapter.auth.repository;

import java.util.Optional;
import org.atlas.infrastructure.persistence.jpa.adapter.auth.entity.JpaAuthUserEntity;
import org.atlas.infrastructure.persistence.jpa.core.repository.JpaBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthUserRepository extends JpaBaseRepository<JpaAuthUserEntity, Integer> {

  Optional<JpaAuthUserEntity> findByUsernameOrEmailOrPhoneNumber(String credential);
}
