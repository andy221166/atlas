package org.atlas.framework.event;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.atlas.framework.event.contract.order.OrderCanceledEvent;
import org.atlas.framework.event.contract.order.OrderConfirmedEvent;
import org.atlas.framework.event.contract.order.OrderCreatedEvent;
import org.atlas.framework.event.contract.product.ProductCreatedEvent;
import org.atlas.framework.event.contract.product.ProductDeletedEvent;
import org.atlas.framework.event.contract.product.ProductUpdatedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.framework.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.framework.event.contract.user.UserRegisteredEvent;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

  // User
  // -----------------------------------------------------------------------------------------------

  USER_REGISTERED(UserRegisteredEvent.class),

  // SearchResponse
  // -----------------------------------------------------------------------------------------------

  PRODUCT_CREATED(ProductCreatedEvent.class),
  PRODUCT_UPDATED(ProductUpdatedEvent.class),
  PRODUCT_DELETED(ProductDeletedEvent.class),

  // Order
  // -----------------------------------------------------------------------------------------------

  ORDER_CREATED(OrderCreatedEvent.class),
  RESERVE_QUANTITY_SUCCEEDED(ReserveQuantitySucceededEvent.class),
  RESERVE_QUANTITY_FAILED(ReserveQuantityFailedEvent.class),
  ORDER_CONFIRMED(OrderConfirmedEvent.class),
  ORDER_CANCELED(OrderCanceledEvent.class);

  private final Class<? extends DomainEvent> eventClass;

  public static EventType findEventType(Class<? extends DomainEvent> eventClass) {
    return Stream.of(EventType.values())
        .filter(eventType -> eventType.getEventClass().equals(eventClass))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("Unknown event class"));
  }
}
