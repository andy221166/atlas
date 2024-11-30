package org.atlas.platform.persistence.jdbc.report.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public BigDecimal findTotalAmount(Date startDate, Date endDate) {
    String sql = """
        select sum(o.amount) 
        from o.orders
        where date(o.created_at) between :startDate and :endDate
        """;

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("startDate", startDate);
    params.addValue("endDate", endDate);

    return namedParameterJdbcTemplate.queryForObject(sql, params, BigDecimal.class);
  }

  public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
    String sql = """
        select o.id, o.user_id, o.first_name, o.last_name, o.amount, o.created_at
        from orders o
        where o.created_at between :startDate and :endDate
        order by o.amount desc
        limit :limit
        """;

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("startDate", startDate);
    params.addValue("endDate", endDate);
    params.addValue("limit", limit);

    return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
      OrderDto order = new OrderDto();
      order.setId(rs.getInt("id"));
      order.setUserId(rs.getInt("user_id"));
      order.setFirstName(rs.getString("first_name"));
      order.setLastName(rs.getString("last_name"));
      order.setAmount(rs.getBigDecimal("amount"));
      order.setCreatedAt(rs.getTimestamp("created_at"));
      return order;
    });
  }

  public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
    String sql = """
        select oi.product_id, oi.product_name, sum(oi.quantity) as total_quantity
        from order_item oi
        inner join orders o on o.id = oi.order_id
        where o.created_at between :startDate and :endDate
        group by oi.product_id, oi.product_name,
        order by total_quantity desc
        limit :limit
        """;

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("startDate", startDate);
    params.addValue("endDate", endDate);
    params.addValue("limit", limit);

    return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
      ProductDto product = new ProductDto();
      product.setId(rs.getInt("id"));
      product.setName(rs.getString("name"));
      product.setTotalQuantity(rs.getInt("total_quantity"));
      return product;
    });
  }

  public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
    String sql = """
        select o.user_id, o.first_name, o.last_name, sum(o.amount) as total_amount
        from orders o
        where o.created_at between :startDate and :endDate
        group by o.user_id, o.first_name, o.last_name
        order by total_amount desc
        limit :limit
        """;

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("startDate", startDate);
    params.addValue("endDate", endDate);
    params.addValue("limit", limit);

    return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
      UserDto user = new UserDto();
      user.setId(rs.getInt("user_id"));
      user.setFirstName(rs.getString("first_name"));
      user.setLastName(rs.getString("last_name"));
      user.setTotalAmount(rs.getBigDecimal("total_amount"));
      return user;
    });
  }

  public int insert(Order order) {
    // Insert order
    String insertOrderSql = """
        insert into orders (user_id, first_name, last_name, amount, created_at)
        values (:userId, :firstName, :lastName, :amount, :createdAt)
        """;
    MapSqlParameterSource orderParams = toOrderParams(order);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(insertOrderSql, orderParams, keyHolder, new String[]{"id"});
    Number insertedOrderId = keyHolder.getKey();
    if (insertedOrderId != null) {
      order.setId(insertedOrderId.intValue());
    } else {
      throw new RuntimeException("Failed to retrieve the inserted ID");
    }

    // Insert order items
    String insertOrderItemSql = """
        insert into order_item (order_id, product_id, product_name, product_price, quantity)
        values (:orderId, :productId, :productName, :productPrice, :quantity)
        """;
    for (OrderItem orderItem : order.getOrderItems()) {
      MapSqlParameterSource orderItemParams = toOrderItemParams(orderItem);
      namedParameterJdbcTemplate.update(insertOrderItemSql, orderItemParams);
    }

    return 1;
  }

  private MapSqlParameterSource toOrderParams(Order order) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", order.getId());
    params.addValue("userId", order.getUser().getId());
    params.addValue("firstName", order.getUser().getFirstName());
    params.addValue("lastName", order.getUser().getLastName());
    params.addValue("amount", order.getAmount());
    params.addValue("createdAt", order.getCreatedAt());
    return params;
  }

  private MapSqlParameterSource toOrderItemParams(OrderItem orderItem) {
    MapSqlParameterSource orderItemParams = new MapSqlParameterSource();
    orderItemParams.addValue("orderId", orderItem.getOrderId());
    orderItemParams.addValue("productId", orderItem.getProduct().getId());
    orderItemParams.addValue("productName", orderItem.getProduct().getName());
    orderItemParams.addValue("productPrice", orderItem.getProduct().getPrice());
    orderItemParams.addValue("quantity", orderItem.getQuantity());
    return orderItemParams;
  }
}
