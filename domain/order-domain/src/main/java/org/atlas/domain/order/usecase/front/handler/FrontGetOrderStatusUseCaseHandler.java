package org.atlas.domain.order.usecase.front.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.usecase.front.model.FrontGetOrderStatusOutput;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.error.AppError;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontGetOrderStatusUseCaseHandler implements
    UseCaseHandler<Integer, FrontGetOrderStatusOutput> {

  private final OrderRepository orderRepository;

  @Override
  public FrontGetOrderStatusOutput handle(Integer orderId) throws Exception {
    OrderEntity orderEntity = orderRepository.findById(orderId)
        .orElseThrow(() -> new DomainException(AppError.ORDER_NOT_FOUND));
    return FrontGetOrderStatusOutput.builder()
        .status(orderEntity.getStatus())
        .canceledReason(orderEntity.getCanceledReason())
        .build();
  }
}
