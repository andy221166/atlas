package org.atlas.service.notification.port.outbound.realtime.sse;

public interface SsePort<K> {

  void notify(SseNotification<K> notification);
}
