package org.atlas.platform.orm.jpa.order.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.platform.orm.jpa.order.entity.JpaOrder;
import org.atlas.platform.orm.jpa.order.entity.JpaOrderItem;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.entity.OrderItemEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

  public static OrderEntity map(JpaOrder jpaOrder) {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setId(jpaOrder.getId());
    orderEntity.setUserId(jpaOrder.getUserId());
    orderEntity.setAmount(jpaOrder.getAmount());
    orderEntity.setStatus(jpaOrder.getStatus());
    orderEntity.setCanceledReason(jpaOrder.getCanceledReason());
    orderEntity.setCreatedAt(jpaOrder.getCreatedAt());
    orderEntity.setUpdatedAt(jpaOrder.getUpdatedAt());
    jpaOrder.getOrderItems().forEach(jpaOrderItem -> {
      OrderItemEntity orderItemEntity = new OrderItemEntity();
      orderItemEntity.setProductId(jpaOrderItem.getProductId());
      orderItemEntity.setProductPrice(jpaOrderItem.getProductPrice());
      orderItemEntity.setQuantity(jpaOrderItem.getQuantity());
      orderEntity.addOrderItem(orderItemEntity);
    });
    return orderEntity;
  }

  public static JpaOrder map(OrderEntity orderEntity) {
    JpaOrder jpaOrder = new JpaOrder();
    jpaOrder.setId(orderEntity.getId());
    jpaOrder.setUserId(orderEntity.getUserId());
    jpaOrder.setAmount(orderEntity.getAmount());
    jpaOrder.setStatus(orderEntity.getStatus());
    jpaOrder.setCanceledReason(orderEntity.getCanceledReason());

    orderEntity.getOrderItemEntities().forEach(orderItemEntity -> {
      JpaOrderItem jpaOrderItem = new JpaOrderItem();
      jpaOrderItem.setProductId(orderItemEntity.getProductId());
      jpaOrderItem.setProductPrice(orderItemEntity.getProductPrice());
      jpaOrderItem.setQuantity(orderItemEntity.getQuantity());
      jpaOrder.addOrderItem(jpaOrderItem);
    });

    return jpaOrder;
  }
}
