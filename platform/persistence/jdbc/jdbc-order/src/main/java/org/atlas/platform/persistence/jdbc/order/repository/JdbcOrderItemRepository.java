package org.atlas.platform.persistence.jdbc.order.repository;

import lombok.RequiredArgsConstructor;
import org.atlas.service.order.domain.OrderItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcOrderItemRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public int insert(OrderItem orderItem) {
    String sql = """
        insert into order_item (order_id, product_id, product_price, quantity)
        values (:orderId, :productId, :productPrice, :quantity)
        """;
    MapSqlParameterSource params = toParams(orderItem);
    return namedParameterJdbcTemplate.update(sql, params);
  }

  private MapSqlParameterSource toParams(OrderItem orderItem) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("orderId", orderItem.getOrderId());
    params.addValue("productId", orderItem.getProductId());
    params.addValue("productPrice", orderItem.getProductPrice());
    params.addValue("quantity", orderItem.getQuantity());
    return params;
  }
}
