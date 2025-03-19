package org.atlas.service.notification.adapter.sse.core;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseEmitterService {

  private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

  public SseEmitter subscribe(String sseEmitterKey) {
    SseEmitter sseEmitter = new SseEmitter();
    sseEmitters.put(sseEmitterKey, sseEmitter);

    sseEmitter.onCompletion(() -> sseEmitters.remove(sseEmitterKey));
    sseEmitter.onTimeout(() -> sseEmitters.remove(sseEmitterKey));

    return sseEmitter;
  }

  public SseEmitter get(String sseEmitterKey) {
    return sseEmitters.get(sseEmitterKey);
  }
}
