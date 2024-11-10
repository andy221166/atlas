package org.atlas.platform.persistence.jdbc.report.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcOrderRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
