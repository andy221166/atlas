package org.atlas.service.order.adapter.persistence.jpa.repository;

import java.util.Optional;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.order.adapter.persistence.jpa.entity.JpaOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaBaseRepository<JpaOrderEntity, Integer> {

  Page<JpaOrderEntity> findByUserId(@Param("userId") Integer userId, Pageable pageable);

  @Query("""
      select o from JpaOrderEntity o
      left join fetch o.orderItems
      where o.id = :id
      """)
  Optional<JpaOrderEntity> findByIdAndFetch(@Param("id") Integer id);
}
