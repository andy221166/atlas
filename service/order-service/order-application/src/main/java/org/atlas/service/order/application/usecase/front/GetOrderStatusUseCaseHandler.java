package org.atlas.service.order.application.usecase.front;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.port.inbound.front.GetOrderStatusUseCase;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetOrderStatusUseCaseHandler implements GetOrderStatusUseCase {

  private final OrderRepository orderRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    OrderEntity orderEntity = orderRepository.findById(input.getOrderId())
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
    return new Output(orderEntity.getStatus());
  }
}
