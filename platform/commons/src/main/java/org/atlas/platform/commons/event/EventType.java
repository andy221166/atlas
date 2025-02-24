package org.atlas.platform.commons.event;

import lombok.Getter;

@Getter
public enum EventType {

  // User
  // -----------------------------------------------------------------------------------------------

  USER_REGISTERED,

  // Product
  // -----------------------------------------------------------------------------------------------

  PRODUCT_CREATED,
  PRODUCT_UPDATED,
  PRODUCT_DELETED,

  // Order
  // -----------------------------------------------------------------------------------------------

  ORDER_CREATED,
  RESERVE_QUANTITY_SUCCEEDED,
  RESERVE_QUANTITY_FAILED,
  ORDER_CONFIRMED,
  ORDER_CANCELED;
}
