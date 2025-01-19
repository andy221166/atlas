package org.atlas.platform.orm.jdbc.aggregator.repository.aggregator;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.orm.jdbc.aggregator.support.OrderExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcAggOrderRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<AggOrder> findByUserId(Integer userId, PagingQuery pagingQuery) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select
          o.id as order_id, o.user_id, o.amount, o.status, o.created_at, o.canceled_reason, o.aggregator,
          oi.id as order_item_id, oi.product_id, oi.product_name, oi.product_price, oi.quantity
        from orders o
        left join order_item oi on o.id = oi.order_id
        where o.user_id = :userId
        order by o.created_at desc
        """
    );

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("userId", userId);

    // Paging
    if (pagingQuery.getLimit() != null && pagingQuery.getOffset() != null) {
      sqlBuilder.append(" limit :limit offset :offset");
      params.addValue("limit", pagingQuery.getLimit())
          .addValue("offset", pagingQuery.getOffset());
    }

    String sql = sqlBuilder.toString();
    return namedParameterJdbcTemplate.query(sql, params, new OrderExtractor());
  }

  public long countByUserId(Integer userId) {
    String sql = """
        select count(o.id) 
        from orders o 
        where o.user_id = :userId
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("userId", userId);
    return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
  }

  public int insert(AggOrder aggOrder) {
    // Insert order
    String insertOrderSql = """
        insert into orders (user_id, amount, status, created_at, canceled_reason, aggregator)
        values (:userId, :firstName, :lastName, :amount, :createdAt, :canceledReason, :aggregator)
        """;
    MapSqlParameterSource orderParams = toOrderParams(aggOrder);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(insertOrderSql, orderParams, keyHolder, new String[]{"id"});
    Number insertedOrderId = keyHolder.getKey();
    if (insertedOrderId != null) {
      aggOrder.setId(insertedOrderId.intValue());
    } else {
      throw new RuntimeException("Failed to retrieve the inserted ID");
    }

    // Insert order items
    String insertOrderItemSql = """
        insert into order_item (order_id, product_id, product_name, product_price, quantity)
        values (:orderId, :productId, :productName, :productPrice, :quantity)
        """;
    for (AggOrderItem aggOrderItem : aggOrder.getOrderItems()) {
      MapSqlParameterSource orderItemParams = toOrderItemParams(aggOrderItem);
      namedParameterJdbcTemplate.update(insertOrderItemSql, orderItemParams);
    }

    return 1;
  }

  private MapSqlParameterSource toOrderParams(AggOrder aggOrder) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", aggOrder.getId());
    params.addValue("userId", aggOrder.getUserId());
    params.addValue("amount", aggOrder.getAmount());
    params.addValue("status", aggOrder.getStatus().name());
    params.addValue("createdAt", aggOrder.getCreatedAt());
    params.addValue("canceledReason", aggOrder.getCanceledReason());
    params.addValue("aggregator", aggOrder.getAggregator().name());
    return params;
  }

  private MapSqlParameterSource toOrderItemParams(AggOrderItem aggOrderItem) {
    MapSqlParameterSource orderItemParams = new MapSqlParameterSource();
    orderItemParams.addValue("orderId", aggOrderItem.getOrderId());
    orderItemParams.addValue("productId", aggOrderItem.getProduct().getId());
    orderItemParams.addValue("productName", aggOrderItem.getProduct().getName());
    orderItemParams.addValue("productPrice", aggOrderItem.getProduct().getPrice());
    orderItemParams.addValue("quantity", aggOrderItem.getQuantity());
    return orderItemParams;
  }
}
