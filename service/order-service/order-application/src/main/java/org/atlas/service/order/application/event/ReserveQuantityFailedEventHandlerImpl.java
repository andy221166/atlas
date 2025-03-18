package org.atlas.service.order.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.domain.shared.OrderStatus;
import org.atlas.service.order.port.inbound.event.ReserveQuantityFailedEventHandler;
import org.atlas.service.order.port.outbound.event.OrderEventPublisher;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantityFailedEventHandlerImpl implements ReserveQuantityFailedEventHandler {

  private final OrderRepository orderRepository;
  private final OrderService orderService;
  private final OrderEventPublisher orderEventPublisher;

  @Override
  @Transactional
  public void handle(ReserveQuantityFailedEvent reserveQuantityFailedEvent) {
    OrderEntity orderEntity = orderService.findProcessingOrder(
        reserveQuantityFailedEvent.getOrderId());
    orderEntity.setStatus(OrderStatus.CANCELED);
    orderEntity.setCanceledReason(reserveQuantityFailedEvent.getError());
    orderRepository.update(orderEntity);

    OrderCanceledEvent orderCanceledEvent = ObjectMapperUtil.getInstance()
        .map(orderEntity, OrderCanceledEvent.class);
    orderEventPublisher.publish(orderCanceledEvent);
  }
}
