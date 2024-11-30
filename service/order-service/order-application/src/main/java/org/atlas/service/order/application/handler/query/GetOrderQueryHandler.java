package org.atlas.service.order.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.order.contract.query.GetOrderQuery;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetOrderQueryHandler implements QueryHandler<GetOrderQuery, Order> {

  private final OrderRepository orderRepository;

  @Override
  @Transactional(readOnly = true)
  public Order handle(GetOrderQuery query) throws Exception {
    return orderRepository.findById(query.getId())
        .orElseThrow(() -> new BusinessException(AppError.ORDER_NOT_FOUND));
  }
}
