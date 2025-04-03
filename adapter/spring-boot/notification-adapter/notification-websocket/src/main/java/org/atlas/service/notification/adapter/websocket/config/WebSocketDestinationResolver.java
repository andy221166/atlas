package org.atlas.service.notification.adapter.websocket.config;

import static org.atlas.service.notification.adapter.websocket.config.WebSocketServerConfig.DESTINATION_PREFIX;

import lombok.experimental.UtilityClass;
import org.atlas.port.outbound.notification.realtime.enums.RealtimeNotificationType;
import org.atlas.port.outbound.notification.realtime.payload.OrderStatusChangedPayload;
import org.atlas.port.outbound.notification.realtime.websocket.WebSocketNotification;

@UtilityClass
public class WebSocketDestinationResolver {

  public static String resolve(WebSocketNotification notification) {
    if (notification == null) {
      throw new IllegalArgumentException("Notification cannot be null.");
    }

    RealtimeNotificationType notificationType = notification.getType();
    if (notificationType == null) {
      throw new IllegalArgumentException("Notification type cannot be null.");
    }

    switch (notificationType) {
      case ORDER_STATUS_CHANGED:
        if (!(notification.getPayload() instanceof OrderStatusChangedPayload payload)) {
          throw new IllegalArgumentException(
              "Payload must be of type OrderStatusChangedPayload for ORDER_STATUS_CHANGED notification.");
        }
        return DESTINATION_PREFIX + "/orders/" + payload.getOrderId() + "/status";
      // Add more cases here for different notification types if needed
      default:
        throw new IllegalArgumentException("Unknown notification type: " + notificationType);
    }
  }
}