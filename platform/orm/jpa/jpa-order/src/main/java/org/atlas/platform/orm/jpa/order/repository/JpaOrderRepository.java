package org.atlas.platform.orm.jpa.order.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.order.entity.JpaOrder;
import org.atlas.service.order.contract.model.OrderStatusStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaBaseRepository<JpaOrder, Integer> {

  Page<JpaOrder> findByUserId(@Param("userId") Integer userId, Pageable pageable);

  @Query("""
      select o from JpaOrder o
      left join fetch o.orderItems
      where o.id = :id
      """)
  Optional<JpaOrder> findByIdAndFetch(@Param("id") Integer id);

  @Query("""
      select new org.atlas.service.order.contract.model.OrderStatusStatisticsDto(
          o.status,
          count(o.id),
          sum(o.amount)
      )
      from JpaOrder o
      where function('date', o.createdAt) = :date
      group by o.status
      """)
  List<OrderStatusStatisticsDto> getOrderStatusStatistics(@Param("date") Date date);
}
