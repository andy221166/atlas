package org.atlas.platform.orm.jpa.order.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.platform.orm.jpa.order.entity.JpaOrder;
import org.atlas.platform.orm.jpa.order.entity.JpaOrderItem;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.OrderItem;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

  public static Order map(JpaOrder jpaOrder) {
    Order order = new Order();
    order.setId(jpaOrder.getId());
    order.setUserId(jpaOrder.getUserId());
    order.setAmount(jpaOrder.getAmount());
    order.setStatus(jpaOrder.getStatus());
    order.setCanceledReason(jpaOrder.getCanceledReason());
    order.setCreatedAt(jpaOrder.getCreatedAt());
    order.setUpdatedAt(jpaOrder.getUpdatedAt());
    jpaOrder.getOrderItems().forEach(jpaOrderItem -> {
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(jpaOrderItem.getProductId());
      orderItem.setProductPrice(jpaOrderItem.getProductPrice());
      orderItem.setQuantity(jpaOrderItem.getQuantity());
      order.addOrderItem(orderItem);
    });
    return order;
  }

  public static JpaOrder map(Order order) {
    JpaOrder jpaOrder = new JpaOrder();
    jpaOrder.setId(order.getId());
    jpaOrder.setUserId(order.getUserId());
    jpaOrder.setAmount(order.getAmount());
    jpaOrder.setStatus(order.getStatus());
    jpaOrder.setCanceledReason(order.getCanceledReason());

    order.getOrderItems().forEach(orderItem -> {
      JpaOrderItem jpaOrderItem = new JpaOrderItem();
      jpaOrderItem.setProductId(orderItem.getProductId());
      jpaOrderItem.setProductPrice(orderItem.getProductPrice());
      jpaOrderItem.setQuantity(orderItem.getQuantity());
      jpaOrder.addOrderItem(jpaOrderItem);
    });

    return jpaOrder;
  }
}
