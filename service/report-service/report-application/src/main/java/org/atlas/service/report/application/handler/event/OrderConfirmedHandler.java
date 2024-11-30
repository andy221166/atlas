package org.atlas.service.report.application.handler.event;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.model.OrderItemDto;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;
import org.atlas.service.report.domain.Product;
import org.atlas.service.report.domain.User;
import org.atlas.service.user.contract.model.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderConfirmedHandler implements EventHandler<OrderConfirmedEvent> {

  private final OrderRepository orderRepository;

  @Override
  public EventType supports() {
    return EventType.ORDER_CONFIRMED;
  }

  @Override
  @Transactional
  public void handle(OrderConfirmedEvent event) {
    Order order = toOrder(event.getOrder());
    orderRepository.insert(order);
  }

  private Order toOrder(OrderDto orderDto) {
    Order order = new Order();
    order.setId(orderDto.getId());
    order.setUser(toUser(orderDto.getUser()));
    orderDto.getOrderItems().forEach(orderItemDto -> {
      OrderItem orderItem = toOrderItem(orderItemDto);
      orderItem.setOrderId(order.getId());
      order.addOrderItem(orderItem);
    });
    order.setAmount(orderDto.getAmount());
    order.setCreatedAt(orderDto.getCreatedAt());
    return order;
  }

  private User toUser(UserDto userDto) {
    return ModelMapperUtil.map(userDto, User.class);
  }

  private OrderItem toOrderItem(OrderItemDto orderItemDto) {
    OrderItem orderItem = new OrderItem();
    orderItem.setProduct(toProduct(orderItemDto.getProduct()));
    orderItem.setQuantity(orderItemDto.getQuantity());
    return orderItem;
  }

  private Product toProduct(ProductDto productDto) {
    return ModelMapperUtil.map(productDto, Product.class);
  }
}