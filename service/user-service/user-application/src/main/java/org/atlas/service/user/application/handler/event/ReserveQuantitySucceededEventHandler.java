package org.atlas.service.user.application.handler.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.base.ConcurrentUtil;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.contract.order.ReserveCreditFailedEvent;
import org.atlas.platform.event.contract.order.ReserveCreditSucceededEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.contract.model.OrderDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveQuantitySucceededEventHandler implements EventHandler<ReserveQuantitySucceededEvent> {

  private final CustomerRepository customerRepository;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_CREDIT_SUCCEEDED;
  }

  @Override
  @Transactional
  public void handle(ReserveQuantitySucceededEvent reserveQuantitySucceededEvent) {
    ConcurrentUtil.sleep(3, 5);
    OrderDto order = reserveQuantitySucceededEvent.getOrder();
    int reserveCreditResult = customerRepository.decreaseCredit(order.getUser().getId(),
        order.getAmount());
    if (reserveCreditResult == 1) {
      log.info("Reserved credit: userId={}, amount={}, eventId={}",
          order.getUser().getId(), order.getAmount(), reserveQuantitySucceededEvent.getEventId());
      ReserveCreditSucceededEvent reserveCreditSucceededEvent = new ReserveCreditSucceededEvent(order);
      eventPublisherTemplate.publish(reserveCreditSucceededEvent);
    } else {
      log.error("Failed to reserve credit: userId={}, amount={}, eventId={}",
          order.getUser().getId(), order.getAmount(), reserveQuantitySucceededEvent.getEventId());
      ReserveCreditFailedEvent reserveCreditFailedEvent =
          new ReserveCreditFailedEvent(order, "Failed to reserve credit");
      eventPublisherTemplate.publish(reserveCreditFailedEvent);
    }
  }
}
