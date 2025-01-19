package org.atlas.platform.orm.jpa.product.repository;

import java.util.List;
import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.product.entity.JpaProduct;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends JpaBaseRepository<JpaProduct, Integer> {

  @Query("select p from JpaProduct p left join fetch p.category where p.id in (:ids)")
  List<JpaProduct> findAllByIdWithCategory(List<Integer> ids);

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
