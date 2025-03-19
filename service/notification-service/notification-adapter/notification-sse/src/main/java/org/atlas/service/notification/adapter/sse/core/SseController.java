package org.atlas.service.notification.adapter.sse.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("notification/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {

  private final SseEmitterService sseEmitterService;

  @GetMapping("/orders/{orderId}/status")
  public SseEmitter streamOrderStatusNotification(@PathVariable("orderId") Integer orderId) {
    String sseEmitterKey = SseEmitterKeyFactory.orderStatusChanged(orderId);
    return sseEmitterService.subscribe(sseEmitterKey);
  }
}
