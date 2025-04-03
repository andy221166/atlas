package org.atlas.service.order.application.usecase.front;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.port.inbound.order.front.GetOrderStatusUseCase;
import org.atlas.service.order.port.outbound.repository.OrderRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetOrderStatusUseCaseHandler implements GetOrderStatusUseCase {

  private final OrderRepositoryPort orderRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public GetOrderStatusOutput handle(GetOrderStatusInput input) throws Exception {
    OrderEntity orderEntity = orderRepositoryPort.findById(input.getOrderId())
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
    return new GetOrderStatusOutput(orderEntity.getStatus());
  }
}
