package org.atlas.platform.event.core;

public enum EventType {

    // Order
    // -----------------------------------------------------------------------------------------------------------------

    // Choreography
    ORDER_CREATED,
    QUANTITY_RESERVED,
    RESERVE_QUANTITY_FAILED,
    CREDIT_RESERVED,
    RESERVE_CREDIT_FAILED,

    // Orchestration
    RESERVE_QUANTITY_REQUEST,
    RESERVE_QUANTITY_REPLY,
    RESERVE_CREDIT_REQUEST,
    RESERVE_CREDIT_REPLY,

    ORDER_CONFIRMED,
    ORDER_CANCELED,
}
