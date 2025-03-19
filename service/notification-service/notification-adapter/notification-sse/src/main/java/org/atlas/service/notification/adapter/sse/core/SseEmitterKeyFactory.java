package org.atlas.service.notification.adapter.sse.core;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SseEmitterKeyFactory {

  public static String orderStatusChanged(Integer orderId) {
    return "orderStatusChanged-" + orderId;
  }
}
