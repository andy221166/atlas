package org.atlas.platform.persistence.jdbc.order.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.persistence.jdbc.order.supports.OrderExtractor;
import org.atlas.service.order.domain.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<Order> findByUserId(Integer userId, PagingRequest pagingRequest) {
    StringBuilder sqlBuilder = new StringBuilder("""
        select o.id as order_id, o.user_id, o.amount, o.status, o.created_at, oi.id as order_item_id, oi.product_id, oi.product_price, oi.quantity
        from orders o
        left join order_item oi on o.id = oi.order_id
        where o.user_id = :userId
        order by o.created_at desc
        """);

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("userId", userId);

    // Paging
    if (pagingRequest.getLimit() != null && pagingRequest.getOffset() != null) {
      sqlBuilder.append(" limit :limit offset :offset");
      params.addValue("limit", pagingRequest.getLimit())
          .addValue("offset", pagingRequest.getOffset());
    }

    String sql = sqlBuilder.toString();
    return namedParameterJdbcTemplate.query(sql, params, new OrderExtractor());
  }

  public Optional<Order> findById(Integer id) {
    String sql = """
        select o.id as order_id, o.user_id, o.amount, o.status, o.canceled_reason, o.created_at, 
           oi.id as order_item_id, oi.product_id, oi.product_price, oi.quantity
        from orders o
        left join order_item oi on o.id = oi.order_id
        where o.id = :id
        limit 1
        """;
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", id);
    List<Order> orders = namedParameterJdbcTemplate.query(sql, params, new OrderExtractor());
    return CollectionUtils.isEmpty(orders) ? Optional.empty() : Optional.of(orders.get(0));
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

  public int insert(Order order) {
    String sql = """
        insert into orders (user_id, amount, status)
        values (:userId, :amount, :status)
        """;
    MapSqlParameterSource params = toParams(order);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
    Number insertedId = keyHolder.getKey();
    if (insertedId != null) {
      order.setId(insertedId.intValue());
    } else {
      throw new RuntimeException("Failed to retrieve the inserted ID");
    }
    return 1;
  }

  public int update(Order order) {
    String sql = """
        update orders o
        set o.status = :status, o.canceled_reason = :canceledReason
        where o.id = :id
        """;
    MapSqlParameterSource params = toParams(order);
    return namedParameterJdbcTemplate.update(sql, params);
  }

  private MapSqlParameterSource toParams(Order order) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", order.getId());
    params.addValue("userId", order.getUserId());
    params.addValue("amount", order.getAmount());
    params.addValue("status", order.getStatus().name());
    params.addValue("canceledReason", order.getCanceledReason());
    return params;
  }
}
