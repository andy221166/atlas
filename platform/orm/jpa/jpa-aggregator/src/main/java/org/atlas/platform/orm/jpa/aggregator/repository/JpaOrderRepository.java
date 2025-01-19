package org.atlas.platform.orm.jpa.aggregator.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.aggregator.entity.JpaOrder;
import org.atlas.platform.orm.jpa.aggregator.projection.OrderDtoProjection;
import org.atlas.platform.orm.jpa.aggregator.projection.ProductDtoProjection;
import org.atlas.platform.orm.jpa.aggregator.projection.UserDtoProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaBaseRepository<JpaOrder, Integer> {



  @Query(
      value = """
          select sum(o.amount)
          from aggOrders o
          where date(o.created_at) between :startDate and :endDate
          """,
      nativeQuery = true
  )
  BigDecimal findTotalAmount(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate
  );
}
