package org.atlas.service.order.application.usecase.front;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.cqrs.query.QueryHandler;
import org.atlas.service.order.contract.query.GetOrderQuery;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetOrderStatusQueryHandler implements QueryHandler<GetOrderQuery, OrderEntity> {

  private final OrderRepository orderRepository;

  @Override
  @Transactional(readOnly = true)
  public OrderEntity handle(GetOrderQuery query) throws Exception {
    return orderRepository.findById(query.getId())
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
  }
}
