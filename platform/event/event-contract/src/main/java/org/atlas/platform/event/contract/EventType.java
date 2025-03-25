package org.atlas.platform.event.contract;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.order.OrderCreatedEvent;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.platform.event.contract.product.ProductUpdatedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.event.contract.user.UserRegisteredEvent;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventType {

  // User
  // -----------------------------------------------------------------------------------------------

  USER_REGISTERED(UserRegisteredEvent.class),

  // Product
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
}
