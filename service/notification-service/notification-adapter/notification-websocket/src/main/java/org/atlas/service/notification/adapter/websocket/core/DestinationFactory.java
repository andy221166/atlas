package org.atlas.service.notification.adapter.websocket.core;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DestinationFactory {

  public static String orderStatusChanged(Integer orderId) {
    return WebSocketServerConfig.DESTINATION_PREFIX + "/order/" + orderId;
  }
}
