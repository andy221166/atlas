package org.atlas.service.order.application.usecase.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.platform.cqrs.query.QueryHandler;
import org.atlas.service.order.application.service.OrderAggregator;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.query.ListOrderQuery;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListOrderQueryHandler implements QueryHandler<ListOrderQuery, PageDto<OrderDto>> {

  private final OrderRepository orderRepository;
  private final OrderAggregator orderAggregator;

  @Override
  @Transactional(readOnly = true)
  public PageDto<OrderDto> handle(ListOrderQuery query) throws Exception {
    Integer currentUserId = UserContext.getCurrentUserId();
    PageDto<OrderEntity> orderPage = orderRepository.findByUserId(currentUserId, query);
    if (orderPage.isEmpty()) {
      return PageDto.empty();
    }

    // Aggregate data from other services
    List<OrderEntity> orderEntityList = orderPage.getRecords();
    List<OrderDto> orderDtoList = orderAggregator.aggregate(orderEntityList, false);

    return PageDto.of(orderDtoList, orderPage.getTotalCount());
  }
}
