package org.atlas.platform.orm.jdbc.aggregator.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.atlas.platform.orm.jdbc.core.NullSafeRowMapper;
import org.atlas.service.aggregator.domain.Aggregator;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class OrderExtractor implements ResultSetExtractor<List<AggOrder>> {

  @Override
  public List<AggOrder> extractData(ResultSet rs) throws SQLException, DataAccessException {
    List<AggOrder> aggOrders = new ArrayList<>();
    AggOrder currentAggOrder = null;
    while (rs.next()) {
      NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
      int orderId = rs.getInt("order_id");
      if (currentAggOrder == null || currentAggOrder.getId() != orderId) {
        currentAggOrder = new AggOrder();
        currentAggOrder.setId(orderId);
        currentAggOrder.setUserId(rowMapper.getInt("user_id"));
        currentAggOrder.setAmount(rowMapper.getBigDecimal("amount"));
        currentAggOrder.setStatus(OrderStatus.valueOf(rowMapper.getString("status")));
        currentAggOrder.setCanceledReason(rowMapper.getString("canceled_reason"));
        currentAggOrder.setCreatedAt(rowMapper.getTimestamp("created_at"));
        currentAggOrder.setAggregator(Aggregator.valueOf(rowMapper.getString("aggregator")));
        aggOrders.add(currentAggOrder);
      }
      if (rs.getObject("order_item_id") != null) {
        AggProduct aggProduct = new AggProduct();
        orderItem.setProductId(rowMapper.getInt("product_id"));
        orderItem.setProductPrice(rowMapper.getBigDecimal("product_price"));
        AggOrderItem aggOrderItem = new AggOrderItem();
        aggOrderItem.setQuantity(rowMapper.getInt("quantity"));
        currentAggOrder.addOrderItem(aggOrderItem);
      }
    }
    return aggOrders;
  }
}
