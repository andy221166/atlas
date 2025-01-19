package org.atlas.service.notification.handler.event.sse;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.EventType;
import org.atlas.platform.event.core.consumer.EventHandler;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.notification.sse.order.OrderStatusNotificationSseService;
import org.atlas.service.notification.model.OrderStatusDto;
import org.atlas.service.order.domain.shared.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCanceledSseHandler implements EventHandler<OrderCanceledEvent> {

  private final OrderStatusNotificationSseService sseService;

  @Override
  public EventType supports() {
    return EventType.ORDER_CANCELED;
  }

  @Override
  public void handle(OrderCanceledEvent event) {
    OrderStatusDto payload = new OrderStatusDto(OrderStatus.CANCELED,
        event.getOrderPayload().getCanceledReason());
    sseService.notify(event.getOrderPayload().getId(), payload);
  }
}
