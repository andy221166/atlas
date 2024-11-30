package org.atlas.platform.persistence.jpa.report.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.platform.persistence.jpa.report.entity.JpaOrder;
import org.atlas.platform.persistence.jpa.report.entity.JpaOrderItem;
import org.atlas.service.report.domain.Order;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

  public static JpaOrder map(Order order) {
    JpaOrder jpaOrder = new JpaOrder();
    jpaOrder.setId(order.getId());
    jpaOrder.setUserId(order.getUser().getId());
    jpaOrder.setFirstName(order.getUser().getFirstName());
    jpaOrder.setLastName(order.getUser().getLastName());
    jpaOrder.setAmount(order.getAmount());
    jpaOrder.setCreatedAt(order.getCreatedAt());

    order.getOrderItems()
        .forEach(orderItem -> {
          JpaOrderItem jpaOrderItem = new JpaOrderItem();
          jpaOrderItem.setProductId(orderItem.getProduct().getId());
          jpaOrderItem.setProductName(orderItem.getProduct().getName());
          jpaOrderItem.setProductPrice(orderItem.getProduct().getPrice());
          jpaOrderItem.setQuantity(orderItem.getQuantity());
          jpaOrder.addOrderItem(jpaOrderItem);
        });

    return jpaOrder;
  }
}
