package org.atlas.service.product.application.handler.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.base.ConcurrentUtil;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantityFailedEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent> {

  private final ProductRepository productRepository;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.ORDER_CREATED;
  }

  @Override
  @Transactional
  public void handle(OrderCreatedEvent orderCreatedEvent) {
    OrderDto order = orderCreatedEvent.getOrder();
    try {
      order.getOrderItems().forEach(orderItem -> {
        ConcurrentUtil.sleep(3, 5);
        int updated = productRepository.decreaseQuantity(orderItem.getProduct().getId(),
            orderItem.getQuantity());
        if (updated == 1) {
          log.info("Reserved quantity: productId={}, amount={}, eventId={}",
              orderItem.getProduct().getId(), orderItem.getQuantity(),
              orderCreatedEvent.getEventId());
        } else {
          log.error("Failed to reserve quantity: productId={}, amount={}, eventId={}",
              orderItem.getProduct().getId(), orderItem.getQuantity(),
              orderCreatedEvent.getEventId());
          throw new RuntimeException(String.format("Product %d has insufficient quantity",
              orderItem.getProduct().getId()));
        }
      });
      ReserveQuantitySucceededEvent reserveQuantitySucceededEvent = new ReserveQuantitySucceededEvent(order);
      eventPublisherTemplate.publish(reserveQuantitySucceededEvent);
    } catch (Exception e) {
      ReserveQuantityFailedEvent reserveQuantityFailedEvent = new ReserveQuantityFailedEvent(order,
          e.getMessage());
      reserveQuantityFailedEvent.setError(e.getMessage());
      eventPublisherTemplate.publish(reserveQuantityFailedEvent);
    }
  }
}
