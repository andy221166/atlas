package org.atlas.infrastructure.persistence.jpa.adapter.order.repository;

import java.util.Optional;
import org.atlas.infrastructure.persistence.jpa.adapter.order.entity.JpaOrderEntity;
import org.atlas.infrastructure.persistence.jpa.core.repository.JpaBaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaBaseRepository<JpaOrderEntity, Integer> {

  @Query("""
      select o
      from JpaOrderEntity o
      left join fetch o.orderItems
      where o.userId = :userId
      """)
  Page<JpaOrderEntity> findByUserId(@Param("userId") Integer userId, Pageable pageable);

  @Query("""
      select o
      from JpaOrderEntity o
      left join fetch o.orderItems
      where o.id = :id
      """)
  Optional<JpaOrderEntity> findByIdAndFetch(@Param("id") Integer id);
}
