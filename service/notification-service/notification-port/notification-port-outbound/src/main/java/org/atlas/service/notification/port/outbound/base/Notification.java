package org.atlas.service.notification.port.outbound.base;

import lombok.Data;
import org.atlas.platform.commons.util.UUIDGenerator;

@Data
public class Notification {

  private final String id;

  public Notification() {
    this.id = UUIDGenerator.generate();
  }
}
