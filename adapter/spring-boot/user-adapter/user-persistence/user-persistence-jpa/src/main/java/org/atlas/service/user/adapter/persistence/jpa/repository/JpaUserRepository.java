package org.atlas.service.user.adapter.persistence.jpa.repository;

import java.util.Optional;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.user.adapter.persistence.jpa.entity.JpaUserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaBaseRepository<JpaUserEntity, Integer> {

  Optional<JpaUserEntity> findByUsername(String username);

  Optional<JpaUserEntity> findByEmail(String email);

  Optional<JpaUserEntity> findByPhoneNumber(String phoneNumber);
}
