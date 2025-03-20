package org.atlas.service.notification.adapter.sse.controller;

import java.util.concurrent.ConcurrentHashMap;
import org.atlas.service.notification.port.outbound.realtime.enums.RealtimeNotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public abstract class SseController<K> {

  protected final ConcurrentHashMap<K, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

  public abstract RealtimeNotificationType getEventType();

  public SseEmitter getSseEmitter(K key) {
    return sseEmitters.get(key);
  }

  protected SseEmitter subscribe(K key) {
    SseEmitter sseEmitter = new SseEmitter();
    sseEmitters.put(key, sseEmitter);

    sseEmitter.onCompletion(() -> sseEmitters.remove(key));
    sseEmitter.onTimeout(() -> sseEmitters.remove(key));

    return sseEmitter;
  }
}
