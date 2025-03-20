package org.atlas.service.notification.port.outbound.realtime.sse;

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
public class SseNotification<K> extends Notification {

  private RealtimeNotificationType type;
  private K key;
  private Object payload;
}
