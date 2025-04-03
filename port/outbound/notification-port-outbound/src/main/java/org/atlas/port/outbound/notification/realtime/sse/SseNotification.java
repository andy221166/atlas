package org.atlas.port.outbound.notification.realtime.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.domain.notification.entity.NotificationEntity;
import org.atlas.port.outbound.notification.realtime.enums.RealtimeNotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SseNotification<K> extends NotificationEntity {

  private RealtimeNotificationType type;
  private K key;
  private Object payload;
}
