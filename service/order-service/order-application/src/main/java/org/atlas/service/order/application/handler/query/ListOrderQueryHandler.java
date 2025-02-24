package org.atlas.service.order.application.handler.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.context.UserContext;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.cqrs.query.QueryHandler;
import org.atlas.service.order.application.service.OrderService;
import org.atlas.service.order.contract.model.OrderDto;
import org.atlas.service.order.contract.query.ListOrderQuery;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListOrderQueryHandler implements QueryHandler<ListOrderQuery, PageDto<OrderDto>> {

  private final OrderRepository orderRepository;
  private final OrderService orderService;

  @Override
  @Transactional(readOnly = true)
  public PageDto<OrderDto> handle(ListOrderQuery query) throws Exception {
    Integer currentUserId = UserContext.getCurrentUserId();
    PageDto<Order> orderPage = orderRepository.findByUserId(currentUserId, query);
    if (orderPage.isEmpty()) {
      return PageDto.empty();
    }

    // Aggregate data from other services
    List<Order> orderList = orderPage.getRecords();
    List<OrderDto> orderDtoList = orderService.aggregate(orderList, false);

    return PageDto.of(orderDtoList, orderPage.getTotalCount());
  }
}
