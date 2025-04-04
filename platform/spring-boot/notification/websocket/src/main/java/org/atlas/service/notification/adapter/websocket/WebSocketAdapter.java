package org.atlas.service.notification.adapter.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.service.notification.adapter.websocket.config.WebSocketDestinationResolver;
import org.atlas.port.outbound.notification.realtime.websocket.WebSocketNotification;
import org.atlas.port.outbound.notification.realtime.websocket.WebSocketPort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "WebSocket")
public class WebSocketAdapter implements WebSocketPort {

  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public void notify(WebSocketNotification notification) {
    log.info("Notifying {}", notification);
    try {
      String destination = WebSocketDestinationResolver.resolve(notification);
      messagingTemplate.convertAndSend(destination, notification.getPayload());
      log.info("Notified {}", notification);
    } catch (Exception e) {
      log.error("Failed to notify {}", notification, e);
    }
  }
}
