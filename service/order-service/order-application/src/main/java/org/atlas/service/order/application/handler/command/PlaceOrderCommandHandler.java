package org.atlas.service.order.application.handler.command;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.context.CurrentUserContext;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.contract.command.PlaceOrderCommand;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.atlas.service.order.domain.OrderItem;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PlaceOrderCommandHandler implements CommandHandler<PlaceOrderCommand, Integer> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  @Transactional
  public Integer handle(PlaceOrderCommand command) {
    Order order = newOrder(command);
    OrderDto orderDto = orderService.aggregate(order, true);

    orderRepository.insert(order);
    orderDto.setId(order.getId());

    postCreateOrder(orderDto);

    return order.getId();
  }

  private Order newOrder(PlaceOrderCommand request) {
    Order order = new Order();
    order.setUserId(CurrentUserContext.getCurrentUserId());
    order.setStatus(OrderStatus.PROCESSING);
    order.setCreatedAt(new Date());
    for (PlaceOrderCommand.OrderItem orderItemRequest : request.getOrderItems()) {
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(orderItemRequest.getProductId());
      orderItem.setQuantity(orderItemRequest.getQuantity());
      order.addOrderItem(orderItem);
    }
    return order;
  }

  private void postCreateOrder(OrderDto order) {
    OrderCreatedEvent event = new OrderCreatedEvent(order);
    eventPublisherTemplate.publish(event);
  }
}
