package org.atlas.service.notification.application.event.sse;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.notification.sse.order.OrderStatusNotificationSseService;
import org.atlas.service.notification.application.event.model.OrderStatusOutput;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseOrderConfirmedEventHandlerImpl implements OrderConfirmedEventHandler {

  private final OrderStatusNotificationSseService sseService;

  @Override
  public void handle(OrderConfirmedEvent event) {
    OrderStatusOutput payload = new OrderStatusOutput(OrderStatus.CONFIRMED);
    sseService.notify(event.getOrderId(), payload);
  }
}
