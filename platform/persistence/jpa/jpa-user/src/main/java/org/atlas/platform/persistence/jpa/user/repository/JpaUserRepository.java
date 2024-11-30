package org.atlas.platform.persistence.jpa.user.repository;

import java.util.Optional;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.persistence.jpa.user.entity.JpaUser;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaBaseRepository<JpaUser, Integer> {

  Optional<JpaUser> findByUsername(String username);

  Optional<JpaUser> findByEmail(String email);
}
