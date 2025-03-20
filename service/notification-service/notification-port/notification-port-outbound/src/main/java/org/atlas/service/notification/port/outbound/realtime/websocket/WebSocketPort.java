package org.atlas.service.notification.port.outbound.realtime.websocket;

public interface WebSocketPort {

  void notify(WebSocketNotification notification);
}
