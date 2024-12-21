package org.atlas.platform.orm.jpa.user.repository;

import java.math.BigDecimal;
import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.user.entity.JpaUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCustomerRepository extends JpaBaseRepository<JpaUser, Integer> {

  @Modifying
  @Query("""
      update JpaCustomer c
      set c.credit = c.credit - :amount
      where c.id = :id and c.credit >= :amount
      """)
  int decreaseCredit(@Param("id") Integer id,
      @Param("amount") BigDecimal amount);
}
