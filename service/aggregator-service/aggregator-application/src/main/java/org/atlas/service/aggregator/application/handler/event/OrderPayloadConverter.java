package org.atlas.service.aggregator.application.handler.event;

import org.atlas.commons.util.mapping.ModelMapperAdapter;
import org.atlas.platform.event.contract.order.payload.OrderItemPayload;
import org.atlas.platform.event.contract.order.payload.OrderPayload;
import org.atlas.service.aggregator.domain.Aggregator;

public class OrderPayloadConverter {

  public static AggOrder convert(OrderPayload orderPayload) {
    AggOrder aggOrder = new AggOrder();
    aggOrder.setId(orderPayload.getId());
    aggOrder.setUserId(orderPayload.getUserId());
    orderPayload.getOrderItems().forEach(orderItemPayload -> {
      AggOrderItem aggOrderItem = convert(orderItemPayload);
      aggOrderItem.setOrderId(orderPayload.getId());
      aggOrder.addOrderItem(aggOrderItem);
    });
    aggOrder.setAmount(orderPayload.getAmount());
    aggOrder.setCreatedAt(orderPayload.getCreatedAt());
    aggOrder.setCanceledReason(orderPayload.getCanceledReason());
    aggOrder.setAggregator(Aggregator.EVENT);
    return aggOrder;
  }

  private static AggOrderItem convert(OrderItemPayload orderItemPayload) {
    AggOrderItem aggOrderItem = new AggOrderItem();
    aggOrderItem.setAggProduct(convert(orderItemPayload.getProduct()));
    aggOrderItem.setQuantity(orderItemPayload.getQuantity());
    return aggOrderItem;
  }

  private static AggProduct convert(ProductPayload productPayload) {
    return ModelMapperAdapter.map(productPayload, AggProduct.class);
  }
}
