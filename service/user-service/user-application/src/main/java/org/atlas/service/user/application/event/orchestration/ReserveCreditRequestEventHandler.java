package org.atlas.service.user.application.event.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.base.ConcurrencyUtil;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.orchestration.ReserveCreditReplyEvent;
import org.atlas.platform.event.contract.order.orchestration.ReserveCreditRequestEvent;
import org.atlas.platform.event.core.publisher.EventPublisherTemplate;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.user.contract.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveCreditRequestEventHandler implements EventHandler<ReserveCreditRequestEvent> {

  private final CustomerRepository customerRepository;
  private final EventPublisherTemplate eventPublisherTemplate;

  @Override
  public EventType supports() {
    return EventType.RESERVE_CREDIT_REQUEST;
  }

  @Override
  @Transactional
  public void handle(ReserveCreditRequestEvent requestEvent) {
    ConcurrencyUtil.sleep(3, 5);
    OrderDto order = requestEvent.getOrder();
    ReserveCreditReplyEvent replyEvent = new ReserveCreditReplyEvent();
    replyEvent.setOrder(order);
    int reserveCreditResult = customerRepository.decreaseCredit(order.getUser().getId(),
        order.getAmount());
    if (reserveCreditResult == 1) {
      log.info("Reserved credit: userId={}, amount={}, eventId={}",
          order.getUser().getId(), order.getAmount(), requestEvent.getEventId());
      replyEvent.setSuccess(true);
    } else {
      log.error("Failed to reserve credit: userId={}, amount={}, eventId={}",
          order.getUser().getId(), order.getAmount(), requestEvent.getEventId());
      replyEvent.setSuccess(false);
      replyEvent.setError("User has insufficient credit");
    }
    eventPublisherTemplate.publish(replyEvent);
  }
}
