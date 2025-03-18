package org.atlas.service.notification.application.event.sse;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.notification.sse.order.OrderStatusNotificationSseService;
import org.atlas.service.notification.application.event.model.OrderStatusOutput;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseOrderCanceledEventHandlerImpl implements OrderCanceledEventHandler {

  private final OrderStatusNotificationSseService sseService;

  @Override
  public void handle(OrderCanceledEvent event) {
    OrderStatusOutput payload = new OrderStatusOutput(OrderStatus.CANCELED,
        event.getCanceledReason());
    sseService.notify(event.getOrderId(), payload);
  }
}
