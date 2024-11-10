package org.atlas.platform.persistence.jpa.report.repository;

import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JpaReportRepository {

    @Query(
        value = """
            select o.id, o.user_id as userId, o.first_name as firstName, o.last_name as lastName, 
                   o.amount, o.created_at as createdAt
            from orders o
            where o.created_at between :startDate and :endDate
            order by o.amount desc
            """,
        nativeQuery = true)
    List<OrderDto> findTopHighestAmountOrders(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              Pageable pageable);
    @Query(
        value = """
            select oi.product_id as id, oi.product_name as name, sum(oi.quantity) as totalQuantity
            from order_item oi
            inner join orders o on o.id = oi.order_id
            where o.created_at between :startDate and :endDate
            group by oi.product_id, oi.product_name
            order by totalQuantity desc
            """,
        nativeQuery = true)
    List<ProductDto> findTopBestSoldProducts(@Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate,
                                             Pageable pageable);

    @Query(
        value = """
            select o.user_id as id, o.first_name as firstName, o.last_name as lastName, sum(o.amount) as totalAmount
            from orders o
            where o.created_at between :startDate and :endDate
            group by o.user_id, o.first_name, o.last_name
            order by totalAmount desc
            """,
        nativeQuery = true)
    List<UserDto> findTopHighestSpentUsers(@Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate,
                                           Pageable pageable);
}
