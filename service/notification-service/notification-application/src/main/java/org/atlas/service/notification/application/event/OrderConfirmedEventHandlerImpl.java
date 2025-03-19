package org.atlas.service.notification.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.service.notification.port.inbound.event.OrderConfirmedEventHandler;
import org.atlas.service.notification.port.outbound.email.OrderEmailPort;
import org.atlas.service.notification.port.outbound.sse.OrderSsePort;
import org.atlas.service.notification.port.outbound.websocket.OrderWebSocketPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedEventHandlerImpl implements OrderConfirmedEventHandler {

  private final OrderEmailPort orderEmailPort;
  private final OrderSsePort orderSsePort;
  private final OrderWebSocketPort orderWebSocketPort;

  @Override
  public void handle(OrderConfirmedEvent event) {
    orderEmailPort.notify(event);
    orderSsePort.notify(event);
    orderWebSocketPort.notify(event);
  }
}
