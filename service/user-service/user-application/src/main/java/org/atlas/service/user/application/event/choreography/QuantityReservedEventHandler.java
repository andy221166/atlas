package org.atlas.service.user.application.event.choreography;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.base.ConcurrencyUtil;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.choreography.CreditReservedEvent;
import org.atlas.platform.event.contract.order.choreography.QuantityReservedEvent;
import org.atlas.platform.event.contract.order.choreography.ReserveCreditFailedEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuantityReservedEventHandler implements EventHandler<QuantityReservedEvent> {

  private final CustomerRepository customerRepository;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.QUANTITY_RESERVED;
  }

  @Override
  @Transactional
  public void handle(QuantityReservedEvent quantityReservedEvent) {
    ConcurrencyUtil.sleep(3, 5);
    OrderDto order = quantityReservedEvent.getOrder();
    int reserveCreditResult = customerRepository.decreaseCredit(order.getUser().getId(),
        order.getAmount());
    if (reserveCreditResult == 1) {
      log.info("Reserved credit: userId={}, amount={}, eventId={}",
          order.getUser().getId(), order.getAmount(), quantityReservedEvent.getEventId());
      CreditReservedEvent creditReservedEvent = new CreditReservedEvent(order);
      eventPublisherTemplate.publish(creditReservedEvent);
    } else {
      log.error("Failed to reserve credit: userId={}, amount={}, eventId={}",
          order.getUser().getId(), order.getAmount(), quantityReservedEvent.getEventId());
      ReserveCreditFailedEvent reserveCreditFailedEvent =
          new ReserveCreditFailedEvent(order, "Failed to reserve credit");
      eventPublisherTemplate.publish(reserveCreditFailedEvent);
    }
  }
}
