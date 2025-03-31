package org.atlas.service.notification.adapter.sse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.util.UUIDGenerator;
import org.atlas.service.notification.adapter.sse.controller.SseController;
import org.atlas.service.notification.port.outbound.realtime.enums.RealtimeNotificationType;
import org.atlas.service.notification.port.outbound.realtime.sse.SseNotification;
import org.atlas.service.notification.port.outbound.realtime.sse.SsePort;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SSE")
public class SseAdapter<K> implements InitializingBean, SsePort<K> {

  private final List<SseController<K>> sseControllers;
  private Map<RealtimeNotificationType, SseController<K>> sseControllersCache;

  @Override
  public void afterPropertiesSet() throws Exception {
    sseControllersCache = sseControllers.stream()
        .collect(Collectors.toMap(SseController::getEventType, Function.identity()));
  }

  @Override
  public void notify(SseNotification<K> notification) {
    log.info("Notifying {}", notification);

    SseController<K> sseController = sseControllersCache.get(notification.getType());
    if (sseController == null) {
      throw new IllegalStateException(
          "No SseController found for notification type: " + notification.getType());
    }

    SseEmitter sseEmitter = sseController.getSseEmitter(notification.getKey());
    if (sseEmitter == null) {
      throw new IllegalStateException("Not found SseEmitter for key " + notification.getKey());
    }

    try {
      SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
          .id(UUIDGenerator.generate())
          .name(notification.getType().name())
          .data(notification.getPayload());
      sseEmitter.send(eventBuilder);
      log.info("Notified {}", notification);
    } catch (IOException e) {
      log.error("Failed to notify {}", notification, e);
      sseEmitter.completeWithError(e);
    }
  }
}
