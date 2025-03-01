package org.atlas.service.product.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.event.EventType;
import org.atlas.platform.commons.util.ConcurrentUtil;
import org.atlas.platform.event.core.EventHandler;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.service.product.application.enums.DecreaseQuantityStrategy;
import org.atlas.platform.event.contract.order.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.service.product.port.outbound.event.publisher.ProductEventPublisher;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent> {

  private final ProductRepository productRepository;
  private final ProductEventPublisher productEventPublisher;

  @Value("${app.decrease-quantity-strategy:constraint")
  private String decreaseQuantityStrategy;

  @Override
  public EventType supports() {
    return EventType.ORDER_CREATED;
  }

  @Override
  @Transactional
  public void handle(OrderCreatedEvent orderCreatedEvent) {
    try {
      orderCreatedEvent.getOrderItems().forEach(orderItem -> {
        decreaseQuantity(orderItem.getProductId(), orderItem.getQuantity());
      });
      ReserveQuantitySucceededEvent reserveQuantitySucceededEvent = new ReserveQuantitySucceededEvent();
      productEventPublisher.publish(reserveQuantitySucceededEvent);
    } catch (Exception e) {
      ReserveQuantityFailedEvent reserveQuantityFailedEvent = new ReserveQuantityFailedEvent(order,
          e.getMessage());
      reserveQuantityFailedEvent.setError(e.getMessage());
      productEventPublisher.publish(reserveQuantityFailedEvent);
    }
  }

  private void decreaseQuantity(Integer productId, Integer quantity) {
    // Mock for processing
    ConcurrentUtil.sleep(3, 5);

    if (DecreaseQuantityStrategy.CONSTRAINT.getValue().equals(decreaseQuantityStrategy)) {
      productRepository.decreaseQuantityWithConstraint(productId, quantity);
    } else if (DecreaseQuantityStrategy.PESSIMISTIC_LOCKING.getValue()
        .equals(decreaseQuantityStrategy)) {
      productRepository.decreaseQuantityWithPessimisticLock(productId, quantity);
    } else if (DecreaseQuantityStrategy.OPTIMISTIC_LOCKING.getValue()
        .equals(decreaseQuantityStrategy)) {
      productRepository.decreaseQuantityWithOptimisticLock(productId, quantity);
    } else {
      throw new IllegalStateException(
          "Unsupported decrease quantity strategy: " + decreaseQuantityStrategy);
    }
  }
}
