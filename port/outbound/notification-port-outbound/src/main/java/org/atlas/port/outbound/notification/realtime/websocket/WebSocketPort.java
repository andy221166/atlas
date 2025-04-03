package org.atlas.port.outbound.notification.realtime.websocket;

public interface WebSocketPort {

  void notify(WebSocketNotification notification);
}
