package org.atlas.service.order.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.order.OrderCanceledEvent;
import org.atlas.platform.event.contract.product.ReserveQuantityFailedEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.port.inbound.order.event.ReserveQuantityFailedEventHandler;
import org.atlas.service.order.port.outbound.event.OrderEventPublisherPort;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantityFailedEventHandlerImpl implements ReserveQuantityFailedEventHandler {

  private final OrderRepositoryPort orderRepositoryPort;
  private final OrderService orderService;
  private final ApplicationConfigService applicationConfigService;
  private final OrderEventPublisherPort orderEventPublisherPort;

  @Override
  @Transactional
  public void handle(ReserveQuantityFailedEvent reserveQuantityFailedEvent) {
    OrderEntity orderEntity = orderService.findProcessingOrder(
        reserveQuantityFailedEvent.getOrderId());
    orderEntity.setStatus(OrderStatus.CANCELED);
    orderEntity.setCanceledReason(reserveQuantityFailedEvent.getError());
    orderRepositoryPort.update(orderEntity);

    OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(
        applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(reserveQuantityFailedEvent, orderCanceledEvent);
    orderCanceledEvent.setCanceledReason(orderEntity.getCanceledReason());
    orderEventPublisherPort.publish(orderCanceledEvent);
  }
}
