package org.atlas.platform.orm.jpa.aggregator.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.platform.orm.jpa.aggregator.entity.JpaOrder;
import org.atlas.platform.orm.jpa.aggregator.entity.JpaOrderItem;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

  public static JpaOrder map(AggOrder aggOrder) {
    JpaOrder jpaOrder = new JpaOrder();
    jpaOrder.setId(aggOrder.getId());
    jpaOrder.setUserId(aggOrder.getUser().getId());
    jpaOrder.setFirstName(aggOrder.getUser().getFirstName());
    jpaOrder.setLastName(aggOrder.getUser().getLastName());
    jpaOrder.setAmount(aggOrder.getAmount());
    jpaOrder.setCreatedAt(aggOrder.getCreatedAt());

    aggOrder.getOrderItemAggs()
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
