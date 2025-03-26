package org.atlas.service.notification.application.event;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.service.notification.port.inbound.event.OrderCanceledEventHandler;
import org.atlas.service.notification.port.outbound.realtime.enums.RealtimeNotificationType;
import org.atlas.service.notification.port.outbound.realtime.model.OrderStatusChangedPayload;
import org.atlas.service.notification.port.outbound.realtime.sse.SseNotification;
import org.atlas.service.notification.port.outbound.realtime.sse.SsePort;
import org.atlas.service.notification.port.outbound.realtime.websocket.WebSocketNotification;
import org.atlas.service.notification.port.outbound.realtime.websocket.WebSocketPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCanceledEventHandlerImpl implements OrderCanceledEventHandler {

  private final SsePort<Integer> ssePort;
  private final WebSocketPort webSocketPort;

  @Override
  public void handle(OrderCanceledEvent event) {
    CompletableFuture.allOf(
        CompletableFuture.runAsync(() -> notifySse(event))
            .exceptionally(e -> {
              log.error("Failed to notify SSE for event {}", event.getEventId(), e);
              return null;
            }),
        CompletableFuture.runAsync(() -> notifyWebSocket(event))
            .exceptionally(e -> {
              log.error("Failed to notify WebSocket for event {}", event.getEventId(), e);
              return null;
            })
    ).join();
  }

  private void notifySse(OrderCanceledEvent event) {
    OrderStatusChangedPayload payload = OrderStatusChangedPayload.from(event);
    SseNotification<Integer> notification = new SseNotification<>(
        RealtimeNotificationType.ORDER_STATUS_CHANGED, event.getOrderId(), payload);
    ssePort.notify(notification);
  }

  private void notifyWebSocket(OrderCanceledEvent event) {
    OrderStatusChangedPayload payload = OrderStatusChangedPayload.from(event);
    WebSocketNotification notification = new WebSocketNotification(
        RealtimeNotificationType.ORDER_STATUS_CHANGED, payload);
    webSocketPort.notify(notification);
  }
}
