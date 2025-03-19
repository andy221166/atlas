package org.atlas.service.notification.adapter.sse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.service.notification.adapter.sse.core.SseEmitterKeyFactory;
import org.atlas.service.notification.adapter.sse.core.SseEmitterService;
import org.atlas.service.notification.adapter.sse.core.SseEvent;
import org.atlas.service.notification.port.outbound.model.OrderStatusChangedPayload;
import org.atlas.service.notification.port.outbound.sse.OrderSsePort;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SSE")
public class OrderSseAdapter implements OrderSsePort {

  private final SseEmitterService sseEmitterService;

  @Override
  public void notify(OrderConfirmedEvent event) {
    log.info("Notifying OrderConfirmedEvent: orderId={}", event.getOrderId());
    String sseEmitterKey = SseEmitterKeyFactory.orderStatusChanged(event.getOrderId());
    SseEmitter sseEmitter = sseEmitterService.get(sseEmitterKey);
    if (sseEmitter != null) {
      try {
        OrderStatusChangedPayload payload = OrderStatusChangedPayload.builder()
            .orderId(event.getOrderId())
            .orderStatus(OrderStatus.CONFIRMED)
            .build();
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
            .name(SseEvent.ORDER_STATUS_CHANGED)
            .data(payload);
        sseEmitter.send(eventBuilder);
      } catch (IOException e) {
        log.error("Failed to notify OrderConfirmedEvent: orderId={}", event.getOrderId(), e);
        sseEmitter.completeWithError(e);
      }
    } else {
      log.error("Failed to notify OrderConfirmedEvent due to not found SseEmitter: orderId={}",
          event.getOrderId());
    }
  }

  @Override
  public void notify(OrderCanceledEvent event) {
    log.info("Notifying OrderCanceledEvent: orderId={}", event.getOrderId());
    String sseEmitterKey = SseEmitterKeyFactory.orderStatusChanged(event.getOrderId());
    SseEmitter sseEmitter = sseEmitterService.get(sseEmitterKey);
    if (sseEmitter != null) {
      try {
        OrderStatusChangedPayload payload = OrderStatusChangedPayload.builder()
            .orderId(event.getOrderId())
            .orderStatus(OrderStatus.CANCELED)
            .canceledReason(event.getCanceledReason())
            .build();
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
            .name(SseEvent.ORDER_STATUS_CHANGED)
            .data(payload);
        sseEmitter.send(eventBuilder);
      } catch (IOException e) {
        log.error("Failed to notify OrderCanceledEvent: orderId={}", event.getOrderId(), e);
        sseEmitter.completeWithError(e);
      }
    } else {
      log.error("Failed to notify OrderCanceledEvent due to not found SseEmitter: orderId={}",
          event.getOrderId());
    }
  }
}
