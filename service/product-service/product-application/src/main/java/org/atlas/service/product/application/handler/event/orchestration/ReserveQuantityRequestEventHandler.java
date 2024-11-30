package org.atlas.service.product.application.handler.event.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.base.ConcurrencyUtil;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.orchestration.ReserveQuantityReplyEvent;
import org.atlas.platform.event.contract.order.orchestration.ReserveQuantityRequestEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveQuantityRequestEventHandler implements
    EventHandler<ReserveQuantityRequestEvent> {

  private final ProductRepository productRepository;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_QUANTITY_REQUEST;
  }

  @Override
  @Transactional
  public void handle(ReserveQuantityRequestEvent requestEvent) {
    OrderDto order = requestEvent.getOrder();
    ReserveQuantityReplyEvent replyEvent = new ReserveQuantityReplyEvent();
    replyEvent.setOrder(order);
    try {
      order.getOrderItems().forEach(orderItem -> {
        ConcurrencyUtil.sleep(3, 5);
        int updated = productRepository.decreaseQuantity(orderItem.getProduct().getId(),
            orderItem.getQuantity());
        if (updated == 1) {
          log.info("Reserved quantity: productId={}, amount={}, eventId={}",
              orderItem.getProduct().getId(), orderItem.getQuantity(), requestEvent.getEventId());
        } else {
          log.error("Failed to reserve quantity: productId={}, amount={}, eventId={}",
              orderItem.getProduct().getId(), orderItem.getQuantity(), requestEvent.getEventId());
          throw new RuntimeException(String.format("Product %d has insufficient quantity",
              orderItem.getProduct().getId()));
        }
      });
      replyEvent.setSuccess(true);
    } catch (Exception e) {
      replyEvent.setSuccess(false);
      replyEvent.setError(e.getMessage());
    }
    eventPublisherTemplate.publish(replyEvent);
  }
}
