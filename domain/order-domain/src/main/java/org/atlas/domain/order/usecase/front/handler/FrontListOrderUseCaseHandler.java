package org.atlas.domain.order.usecase.front.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.repository.FindOrderCriteria;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.domain.order.service.OrderAggregator;
import org.atlas.domain.order.usecase.front.model.FrontListOrderInput;
import org.atlas.framework.context.Contexts;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontListOrderUseCaseHandler implements
    UseCaseHandler<FrontListOrderInput, PagingResult<OrderEntity>> {

  private final OrderRepository orderRepository;
  private final OrderAggregator orderAggregator;

  @Override
  public PagingResult<OrderEntity> handle(FrontListOrderInput input) throws Exception {
    // Query order
    FindOrderCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindOrderCriteria.class);
    criteria.setUserId(Contexts.getUserId());
    PagingResult<OrderEntity> orderEntityPage = orderRepository.findByCriteria(criteria,
        input.getPagingRequest());
    if (orderEntityPage.checkEmpty()) {
      return PagingResult.empty();
    }

    // Load users and products
    orderAggregator.aggregate(orderEntityPage.getData(), true);

    return orderEntityPage;
  }
}
