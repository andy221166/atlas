package org.atlas.service.notification.adapter.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.service.notification.adapter.websocket.core.DestinationFactory;
import org.atlas.service.notification.port.outbound.model.OrderStatusChangedPayload;
import org.atlas.service.notification.port.outbound.websocket.OrderWebSocketPort;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "WebSocket")
public class OrderWebSocketAdapter implements OrderWebSocketPort {

  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public void notify(OrderConfirmedEvent event) {
    log.info("Notifying OrderConfirmedEvent: orderId={}", event.getOrderId());
    try {
      String destination = DestinationFactory.orderStatusChanged(event.getOrderId());
      OrderStatusChangedPayload payload = OrderStatusChangedPayload.builder()
          .orderId(event.getOrderId())
          .orderStatus(OrderStatus.CONFIRMED)
          .build();
      messagingTemplate.convertAndSend(destination, payload);
    } catch (Exception e) {
      log.error("Failed to notify OrderConfirmedEvent: orderId={}", event.getOrderId(), e);
    }
  }

  @Override
  public void notify(OrderCanceledEvent event) {
    log.info("Notifying OrderCanceledEvent: orderId={}", event.getOrderId());
    try {
      String destination = DestinationFactory.orderStatusChanged(event.getOrderId());
      OrderStatusChangedPayload payload = OrderStatusChangedPayload.builder()
          .orderId(event.getOrderId())
          .orderStatus(OrderStatus.CANCELED)
          .canceledReason(event.getCanceledReason())
          .build();
      messagingTemplate.convertAndSend(destination, payload);
    } catch (Exception e) {
      log.error("Failed to notify OrderCanceledEvent: orderId={}", event.getOrderId(), e);
    }
  }
}
