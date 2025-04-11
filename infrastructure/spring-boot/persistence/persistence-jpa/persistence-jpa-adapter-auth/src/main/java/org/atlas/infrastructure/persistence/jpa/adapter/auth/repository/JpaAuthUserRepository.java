package org.atlas.infrastructure.persistence.jpa.adapter.auth.repository;

import java.util.Optional;
import org.atlas.infrastructure.persistence.jpa.adapter.auth.entity.JpaAuthUserEntity;
import org.atlas.infrastructure.persistence.jpa.core.repository.JpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAuthUserRepository extends JpaBaseRepository<JpaAuthUserEntity, Integer> {

  @Query("""
        SELECT u
        FROM JpaAuthUserEntity u
        WHERE u.username = :identifier
           OR u.email = :identifier
           OR u.phoneNumber = :identifier
      """)
  Optional<JpaAuthUserEntity> findByIdentifier(@Param("identifier") String identifier);
}
