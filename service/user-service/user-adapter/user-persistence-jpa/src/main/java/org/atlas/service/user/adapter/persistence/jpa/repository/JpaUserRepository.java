package org.atlas.service.user.adapter.persistence.jpa.repository;

import java.util.Optional;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.user.adapter.persistence.jpa.entity.JpaUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaBaseRepository<JpaUser, Integer> {

  @Query("""
          SELECT u FROM JpaUser u
          WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
      """)
  Page<JpaUser> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

  Optional<JpaUser> findByUsername(String username);

  Optional<JpaUser> findByEmail(String email);

  Optional<JpaUser> findByPhoneNumber(String phoneNumber);
}
