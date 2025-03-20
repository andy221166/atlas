package org.atlas.service.notification.port.outbound.realtime.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.service.notification.port.outbound.base.Notification;
import org.atlas.service.notification.port.outbound.realtime.enums.RealtimeNotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WebSocketNotification extends Notification {

  private RealtimeNotificationType type;
  private Object payload;
}
