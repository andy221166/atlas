package org.atlas.service.product.application.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.util.ConcurrentUtil;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.port.inbound.event.OrderCreatedEventHandler;
import org.atlas.service.product.port.outbound.event.ProductEventPublisherPort;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandlerImpl implements OrderCreatedEventHandler {

  private final ProductRepositoryPort productRepositoryPort;
  private final ApplicationConfigService applicationConfigService;
  private final ProductEventPublisherPort productEventPublisherPort;

  @Value("${app.decrease-quantity-strategy:constraint")
  private String decreaseQuantityStrategy;

  @Override
  @Transactional
  public void handle(OrderCreatedEvent orderCreatedEvent) {
    try {
      orderCreatedEvent.getOrderItems().forEach(orderItem -> {
        decreaseQuantity(orderItem.getProduct().getId(), orderItem.getQuantity());
      });
      ReserveQuantitySucceededEvent reserveQuantitySucceededEvent =
          new ReserveQuantitySucceededEvent(applicationConfigService.getApplicationName());
      ObjectMapperUtil.getInstance().merge(orderCreatedEvent, reserveQuantitySucceededEvent);
      productEventPublisherPort.publish(reserveQuantitySucceededEvent);
    } catch (Exception e) {
      ReserveQuantityFailedEvent reserveQuantityFailedEvent =
          new ReserveQuantityFailedEvent(applicationConfigService.getApplicationName());
      ObjectMapperUtil.getInstance().merge(orderCreatedEvent, reserveQuantityFailedEvent);
      reserveQuantityFailedEvent.setError(e.getMessage());
      productEventPublisherPort.publish(reserveQuantityFailedEvent);
    }
  }

  private void decreaseQuantity(Integer productId, Integer quantity) {
    // Mock for processing
    ConcurrentUtil.sleep(3, 5);

    if (DecreaseQuantityStrategy.CONSTRAINT.getValue().equals(decreaseQuantityStrategy)) {
      productRepositoryPort.decreaseQuantityWithConstraint(productId, quantity);
    } else if (DecreaseQuantityStrategy.PESSIMISTIC_LOCKING.getValue()
        .equals(decreaseQuantityStrategy)) {
      productRepositoryPort.decreaseQuantityWithPessimisticLock(productId, quantity);
    } else if (DecreaseQuantityStrategy.OPTIMISTIC_LOCKING.getValue()
        .equals(decreaseQuantityStrategy)) {
      productRepositoryPort.decreaseQuantityWithOptimisticLock(productId, quantity);
    } else {
      throw new IllegalStateException(
          "Unsupported decrease quantity strategy: " + decreaseQuantityStrategy);
    }
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  private enum DecreaseQuantityStrategy {

    CONSTRAINT("constraint"),
    OPTIMISTIC_LOCKING("optimistic_locking"),
    PESSIMISTIC_LOCKING("pessimistic_locking");

    private final String value;
  }
}
