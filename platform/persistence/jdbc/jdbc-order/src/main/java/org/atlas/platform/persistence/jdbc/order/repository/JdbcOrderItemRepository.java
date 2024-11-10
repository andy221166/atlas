package org.atlas.platform.persistence.jdbc.order.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.service.order.domain.OrderItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcOrderItemRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int insert(OrderItem orderItem) {
        String sql = """
            insert into order_item (order_id, product_id, product_price, quantity)
            values (:orderId, :productId, :productPrice, :quantity)
            """;
        MapSqlParameterSource orderItemParams = toParams(orderItem);
        return namedParameterJdbcTemplate.update(sql, orderItemParams);
    }

    private MapSqlParameterSource toParams(OrderItem orderItem) {
        MapSqlParameterSource orderItemParams = new MapSqlParameterSource();
        orderItemParams.addValue("orderId", orderItem.getOrderId());
        orderItemParams.addValue("productId", orderItem.getProductId());
        orderItemParams.addValue("productPrice", orderItem.getProductPrice());
        orderItemParams.addValue("quantity", orderItem.getQuantity());
        return orderItemParams;
    }
}
