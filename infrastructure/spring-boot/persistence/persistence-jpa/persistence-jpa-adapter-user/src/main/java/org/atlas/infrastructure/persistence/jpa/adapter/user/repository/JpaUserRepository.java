package org.atlas.infrastructure.persistence.jpa.adapter.user.repository;

import org.atlas.domain.user.repository.FindUserCriteria;
import org.atlas.infrastructure.persistence.jpa.adapter.user.entity.JpaUserEntity;
import org.atlas.infrastructure.persistence.jpa.core.repository.JpaBaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaBaseRepository<JpaUserEntity, Integer> {

  @Query("""
      select u
      from JpaUserEntity u
      where 1 = 1
        and (:#{#criteria.id} is null or u.id = :#{#criteria.id})
        and (:#{#criteria.username} is null or u.username = :#{#criteria.username})
        and (:#{#criteria.email} is null or u.email = :#{#criteria.email})
        and (:#{#criteria.phoneNumber} is null or u.phoneNumber = :#{#criteria.phoneNumber})
      """)
  Page<JpaUserEntity> findByCriteria(@Param("criteria") FindUserCriteria criteria,
      Pageable pageable);
}
