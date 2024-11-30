package org.atlas.platform.persistence.jdbc.order.supports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.atlas.platform.persistence.jdbc.core.NullSafeRowMapper;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.OrderItem;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class OrderExtractor implements ResultSetExtractor<List<Order>> {

  @Override
  public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
    List<Order> orders = new ArrayList<>();
    Order currentOrder = null;
    while (rs.next()) {
      NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
      int orderId = rs.getInt("order_id");
      if (currentOrder == null || currentOrder.getId() != orderId) {
        currentOrder = new Order();
        currentOrder.setId(orderId);
        currentOrder.setUserId(rowMapper.getInt("user_id"));
        currentOrder.setAmount(rowMapper.getBigDecimal("amount"));
        currentOrder.setStatus(OrderStatus.valueOf(rowMapper.getString("status")));
        currentOrder.setCanceledReason(rowMapper.getString("canceled_reason"));
        currentOrder.setCreatedAt(rowMapper.getTimestamp("created_at"));
        currentOrder.setUpdatedAt(rowMapper.getTimestamp("updated_at"));
        orders.add(currentOrder);
      }
      if (rs.getObject("order_item_id") != null) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(rowMapper.getInt("product_id"));
        orderItem.setProductPrice(rowMapper.getBigDecimal("product_price"));
        orderItem.setQuantity(rowMapper.getInt("quantity"));
        currentOrder.addOrderItem(orderItem);
      }
    }
    return orders;
  }
}
