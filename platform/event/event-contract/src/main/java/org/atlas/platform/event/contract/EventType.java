package org.atlas.platform.event.contract;

public enum EventType {

  // Order
  // -----------------------------------------------------------------------------------------------------------------

  ORDER_CREATED,
  RESERVE_QUANTITY_SUCCEEDED,
  RESERVE_QUANTITY_FAILED,
  RESERVE_CREDIT_SUCCEEDED,
  RESERVE_CREDIT_FAILED,
  ORDER_CONFIRMED,
  ORDER_CANCELED,
}
