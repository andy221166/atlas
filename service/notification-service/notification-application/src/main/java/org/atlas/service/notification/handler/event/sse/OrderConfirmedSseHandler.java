package org.atlas.service.notification.handler.event.sse;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.core.model.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.notification.sse.order.OrderStatusNotificationSseService;
import org.atlas.service.notification.model.OrderStatusDto;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedSseHandler implements EventHandler<OrderConfirmedEvent> {

  private final OrderStatusNotificationSseService sseService;

  @Override
  public EventType supports() {
    return EventType.ORDER_CONFIRMED;
  }

  @Override
  public void handle(OrderConfirmedEvent event) {
    OrderStatusDto payload = new OrderStatusDto(OrderStatus.CONFIRMED);
    sseService.notify(event.getOrderPayload().getId(), payload);
  }
}
