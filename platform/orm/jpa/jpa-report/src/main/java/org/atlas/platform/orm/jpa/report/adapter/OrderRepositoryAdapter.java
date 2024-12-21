package org.atlas.platform.orm.jpa.report.adapter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.orm.jpa.report.entity.JpaOrder;
import org.atlas.platform.orm.jpa.report.mapper.OrderMapper;
import org.atlas.platform.orm.jpa.report.projection.OrderDtoProjection;
import org.atlas.platform.orm.jpa.report.projection.ProductDtoProjection;
import org.atlas.platform.orm.jpa.report.projection.UserDtoProjection;
import org.atlas.platform.orm.jpa.report.repository.JpaOrderRepository;
import org.atlas.service.report.contract.model.OrderDto;
import org.atlas.service.report.contract.model.ProductDto;
import org.atlas.service.report.contract.model.UserDto;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;

  @Override
  public BigDecimal findTotalAmount(Date startDate, Date endDate) {
    return jpaOrderRepository.findTotalAmount(startDate, endDate);
  }

  @Override
  public List<OrderDto> findTopHighestAmountOrders(Date startDate, Date endDate, int limit) {
    List<OrderDtoProjection> projections = jpaOrderRepository.findTopHighestAmountOrders(
        startDate, endDate, PageRequest.of(0, limit));
    return projections.stream()
        .map(projection -> ModelMapperUtil.map(projection, OrderDto.class))
        .toList();
  }

  @Override
  public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
    List<ProductDtoProjection> projections = jpaOrderRepository.findTopBestSoldProducts(
        startDate, endDate, PageRequest.of(0, limit));
    return projections.stream()
        .map(projection -> ModelMapperUtil.map(projection, ProductDto.class))
        .toList();
  }

  @Override
  public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
    List<UserDtoProjection> projections = jpaOrderRepository.findTopHighestSpentUsers(
        startDate, endDate, PageRequest.of(0, limit));
    return projections.stream()
        .map(projection -> ModelMapperUtil.map(projection, UserDto.class))
        .toList();
  }

  @Override
  public void insert(Order order) {
    JpaOrder jpaOrder = OrderMapper.map(order);
    jpaOrderRepository.insert(jpaOrder);
    order.setId(jpaOrder.getId());
  }
}
