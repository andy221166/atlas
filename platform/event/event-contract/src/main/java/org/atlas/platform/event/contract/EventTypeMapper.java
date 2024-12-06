package org.atlas.platform.event.contract;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.ReserveCreditSuccessEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantitySuccessEvent;
import org.atlas.platform.event.contract.order.ReserveCreditFailedEvent;
import org.atlas.platform.event.contract.order.ReserveQuantityFailedEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventTypeMapper {

  private static final Map<EventType, Class<? extends DomainEvent>> EVENT_TYPE_TO_CLASS = new HashMap<>();
  private static final Map<Class<? extends DomainEvent>, EventType> CLASS_TO_EVENT_TYPE = new HashMap<>();

  static {
    // Order events
    EVENT_TYPE_TO_CLASS.put(EventType.ORDER_CREATED, OrderCreatedEvent.class);
    EVENT_TYPE_TO_CLASS.put(EventType.RESERVE_QUANTITY_SUCCESS, ReserveQuantitySuccessEvent.class);
    EVENT_TYPE_TO_CLASS.put(EventType.RESERVE_QUANTITY_FAILED, ReserveQuantityFailedEvent.class);
    EVENT_TYPE_TO_CLASS.put(EventType.RESERVE_CREDIT_SUCCESS, ReserveCreditSuccessEvent.class);
    EVENT_TYPE_TO_CLASS.put(EventType.RESERVE_CREDIT_FAILED, ReserveCreditFailedEvent.class);
    EVENT_TYPE_TO_CLASS.put(EventType.ORDER_CONFIRMED, OrderConfirmedEvent.class);
    EVENT_TYPE_TO_CLASS.put(EventType.ORDER_CANCELED, OrderCanceledEvent.class);

    // Build the reverse mapping from DomainEvent subclass to EventType
    EVENT_TYPE_TO_CLASS.forEach((key, value) -> CLASS_TO_EVENT_TYPE.put(value, key));
  }

  public static EventType getEventType(Class<? extends DomainEvent> eventClass) {
    return Optional.ofNullable(CLASS_TO_EVENT_TYPE.get(eventClass))
        .orElseThrow(() -> new IllegalArgumentException(
            "No EventType found for class: " + eventClass.getName()));
  }

  public static Class<? extends DomainEvent> getEventClass(EventType eventType) {
    return Optional.ofNullable(EVENT_TYPE_TO_CLASS.get(eventType))
        .orElseThrow(() -> new IllegalArgumentException(
            "No concrete DomainEvent class found for EventType: " + eventType));
  }
}

