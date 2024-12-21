package org.atlas.platform.orm.jpa.product.repository;

import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.product.entity.JpaProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaBaseRepository<JpaProduct, Integer> {

  @Modifying
  @Query(
      """
          update JpaProduct p 
          set p.quantity = p.quantity + :amount 
          where p.id = :id
          """)
  int increaseQuantity(@Param("id") Integer id,
      @Param("amount") Integer amount);

  @Modifying
  @Query(
      """
          update JpaProduct p 
          set p.quantity = p.quantity - :amount 
          where p.id = :id and p.quantity >= :amount
          """)
  int decreaseQuantity(@Param("id") Integer id,
      @Param("amount") Integer amount);
}
