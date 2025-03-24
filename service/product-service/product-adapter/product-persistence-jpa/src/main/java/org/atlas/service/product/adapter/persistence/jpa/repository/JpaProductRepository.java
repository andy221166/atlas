package org.atlas.service.product.adapter.persistence.jpa.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntity;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaBaseRepository<JpaProductEntity, Integer>{

  @Query("""
        select p
        from JpaProductEntity p
        left join fetch p.detail
        left join fetch p.attributes
        left join fetch p.brand
        left join fetch p.categories
        where p.id = :id
      """)
  Optional<JpaProductEntity> findByIdWithAssociations(@Param("id") Integer id);

  @Query("""
        select p from JpaProductEntity p
        where p.id = :id
      """)
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<JpaProductEntity> findByIdWithAssociationsAndLock(@Param("id") Integer id);

  @Modifying
  @Query("""
      update JpaProductEntity p
      set p.quantity = p.quantity - :decrement
      where p.id = :id
      and p.quantity >= :decrement
      """)
  int decreaseQuantity(@Param("id") Integer id, @Param("quantity") Integer decrement);
}
