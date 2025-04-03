package org.atlas.service.order.application.event;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.order.OrderConfirmedEvent;
import org.atlas.platform.event.contract.product.ReserveQuantitySucceededEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.shared.OrderStatus;
import org.atlas.port.inbound.order.event.ReserveQuantitySucceededEventHandler;
import org.atlas.service.order.port.outbound.event.OrderEventPublisherPort;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReserveQuantitySucceededEventHandlerImpl implements
    ReserveQuantitySucceededEventHandler {

  private final OrderRepositoryPort orderRepositoryPort;
  private final OrderEventPublisherPort orderEventPublisherPort;
  private final OrderService orderService;
  private final ApplicationConfigService applicationConfigService;

  @Override
  @Transactional
  public void handle(ReserveQuantitySucceededEvent reserveQuantitySucceededEvent) {
    OrderEntity orderEntity = orderService.findProcessingOrder(
        reserveQuantitySucceededEvent.getOrderId());
    orderEntity.setStatus(OrderStatus.CONFIRMED);
    orderRepositoryPort.update(orderEntity);

    OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(
        applicationConfigService.getApplicationName());
    ObjectMapperUtil.getInstance().merge(reserveQuantitySucceededEvent, orderConfirmedEvent);
    orderEventPublisherPort.publish(orderConfirmedEvent);
  }
}
