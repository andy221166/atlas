package org.atlas.service.notification.adapter.sse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.service.notification.port.outbound.realtime.enums.RealtimeNotificationType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderStatusChangedController extends SseController<Integer> {

  @Override
  public RealtimeNotificationType getEventType() {
    return RealtimeNotificationType.ORDER_STATUS_CHANGED;
  }

  @GetMapping("/notification/sse/orders/{orderId}/status")
  public SseEmitter streamOrderStatusNotification(@PathVariable("orderId") Integer orderId) {
    return subscribe(orderId);
  }
}
