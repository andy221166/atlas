package org.atlas.platform.orm.jpa.report.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.report.entity.JpaOrder;
import org.atlas.platform.orm.jpa.report.projection.OrderDtoProjection;
import org.atlas.platform.orm.jpa.report.projection.ProductDtoProjection;
import org.atlas.platform.orm.jpa.report.projection.UserDtoProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaBaseRepository<JpaOrder, Integer> {

  @Query(
      value = """
          select sum(o.amount)
          from orders o
          where date(o.created_at) between :startDate and :endDate
          """,
      nativeQuery = true
  )
  BigDecimal findTotalAmount(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate
  );

  @Query(
      value = """
          select o.id, o.user_id as userId, o.first_name as firstName, o.last_name as lastName, 
                 o.amount, o.created_at as createdAt
          from orders o
          where date(o.created_at) between :startDate and :endDate
          order by o.amount desc
          """,
      nativeQuery = true
  )
  List<OrderDtoProjection> findTopHighestAmountOrders(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      Pageable pageable
  );

  @Query(
      value = """
          select oi.product_id as id, oi.product_name as name, sum(oi.quantity) as totalQuantity
          from order_item oi
          inner join orders o on o.id = oi.order_id
          where date(o.created_at) between :startDate and :endDate
          group by oi.product_id, oi.product_name
          order by totalQuantity desc
          """,
      nativeQuery = true
  )
  List<ProductDtoProjection> findTopBestSoldProducts(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      Pageable pageable
  );

  @Query(
      value = """
          select o.user_id as id, o.first_name as firstName, o.last_name as lastName, sum(o.amount) as totalAmount
          from orders o
          where date(o.created_at) between :startDate and :endDate
          group by o.user_id, o.first_name, o.last_name
          order by totalAmount desc
          """,
      nativeQuery = true
  )
  List<UserDtoProjection> findTopHighestSpentUsers(
      @Param("startDate") Date startDate,
      @Param("endDate") Date endDate,
      Pageable pageable
  );
}
