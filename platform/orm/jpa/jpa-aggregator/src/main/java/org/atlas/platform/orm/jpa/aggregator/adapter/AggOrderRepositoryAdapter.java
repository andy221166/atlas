package org.atlas.platform.orm.jpa.aggregator.adapter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperAdapter;
import org.atlas.platform.orm.jpa.aggregator.entity.JpaOrder;
import org.atlas.platform.orm.jpa.aggregator.mapper.OrderMapper;
import org.atlas.platform.orm.jpa.aggregator.projection.OrderDtoProjection;
import org.atlas.platform.orm.jpa.aggregator.projection.ProductDtoProjection;
import org.atlas.platform.orm.jpa.aggregator.projection.UserDtoProjection;
import org.atlas.platform.orm.jpa.aggregator.repository.JpaOrderRepository;
import org.atlas.service.aggregator.contract.model.OrderDto;
import org.atlas.service.aggregator.contract.model.ProductDto;
import org.atlas.service.aggregator.contract.repository.aggregator.AggOrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggOrderRepositoryAdapter implements AggOrderRepository {

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
        .map(projection -> ModelMapperAdapter.map(projection, OrderDto.class))
        .toList();
  }

  @Override
  public List<ProductDto> findTopBestSoldProducts(Date startDate, Date endDate, int limit) {
    List<ProductDtoProjection> projections = jpaOrderRepository.findTopBestSoldProducts(
        startDate, endDate, PageRequest.of(0, limit));
    return projections.stream()
        .map(projection -> ModelMapperAdapter.map(projection, ProductDto.class))
        .toList();
  }

  @Override
  public List<UserDto> findTopHighestSpentUsers(Date startDate, Date endDate, int limit) {
    List<UserDtoProjection> projections = jpaOrderRepository.findTopHighestSpentUsers(
        startDate, endDate, PageRequest.of(0, limit));
    return projections.stream()
        .map(projection -> ModelMapperAdapter.map(projection, UserDto.class))
        .toList();
  }

  @Override
  public void insert(AggOrder aggOrder) {
    JpaOrder jpaOrder = OrderMapper.map(aggOrder);
    jpaOrderRepository.insert(jpaOrder);
    aggOrder.setId(jpaOrder.getId());
  }
}
