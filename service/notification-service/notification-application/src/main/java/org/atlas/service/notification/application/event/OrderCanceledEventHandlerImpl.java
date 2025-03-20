package org.atlas.service.notification.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.atlas.service.notification.port.outbound.sse.SsePort;
import org.atlas.service.notification.port.outbound.websocket.OrderWebSocketPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCanceledEventHandlerImpl implements OrderCanceledEventHandler {

  private final SsePort ssePort;
  private final OrderWebSocketPort orderWebSocketPort;

  @Override
  public void handle(OrderCanceledEvent event) {
    ssePort.notify(event);
    orderWebSocketPort.notify(event);
  }
}
