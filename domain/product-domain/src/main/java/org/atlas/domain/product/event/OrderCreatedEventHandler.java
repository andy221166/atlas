package org.atlas.domain.product.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.domain.product.port.messaging.ProductMessagePublisherPort;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.shared.enums.DecreaseQuantityStrategy;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.framework.event.handler.EventHandler;

@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler implements EventHandler<OrderCreatedEvent> {

  private final ProductRepository productRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final ProductMessagePublisherPort productMessagePublisherPort;

  @Override
  public void handle(OrderCreatedEvent orderCreatedEvent) {
    try {
      orderCreatedEvent.getOrderItems().forEach(orderItem ->
          decreaseQuantity(orderItem.getProduct().getId(), orderItem.getQuantity()));
      ReserveQuantitySucceededEvent reserveQuantitySucceededEvent =
          new ReserveQuantitySucceededEvent(applicationConfigPort.getApplicationName());
      reserveQuantitySucceededEvent.merge(orderCreatedEvent);
      productMessagePublisherPort.publish(reserveQuantitySucceededEvent);
    } catch (Exception e) {
      log.error("Failed to handle event {}", orderCreatedEvent.getEventId(), e);
      ReserveQuantityFailedEvent reserveQuantityFailedEvent =
          new ReserveQuantityFailedEvent(applicationConfigPort.getApplicationName());
      reserveQuantityFailedEvent.merge(orderCreatedEvent);
      reserveQuantityFailedEvent.setError(e.getMessage());
      productMessagePublisherPort.publish(reserveQuantityFailedEvent);
    }
  }

  private void decreaseQuantity(Integer productId, Integer quantity) {
    DecreaseQuantityStrategy decreaseQuantityStrategy =
        applicationConfigPort.getDecreaseQuantityStrategy();
    switch (applicationConfigPort.getDecreaseQuantityStrategy()) {
      case CONSTRAINT -> productRepository.decreaseQuantityWithConstraint(productId, quantity);
      case PESSIMISTIC_LOCKING ->
          productRepository.decreaseQuantityWithPessimisticLock(productId, quantity);
      default -> throw new UnsupportedOperationException(
          "Unsupported decrease quantity strategy: " + decreaseQuantityStrategy);
    }
  }
}
