package org.atlas.port.outbound.notification.realtime.sse;

public interface SsePort<K> {

  void notify(SseNotification<K> notification);
}
