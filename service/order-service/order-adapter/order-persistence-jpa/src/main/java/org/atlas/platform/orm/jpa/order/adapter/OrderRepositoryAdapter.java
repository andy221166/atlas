package org.atlas.platform.orm.jpa.order.adapter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.orm.jpa.order.entity.JpaOrder;
import org.atlas.platform.orm.jpa.order.mapper.OrderMapper;
import org.atlas.platform.orm.jpa.order.repository.JpaOrderRepository;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;

  @Override
  public PageDto<OrderEntity> findByUserId(Integer userId, PagingRequest pagingRequest) {
    Pageable pageable = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize());
    Page<JpaOrder> jpaOrderPage = jpaOrderRepository.findByUserId(userId, pageable);
    Page<OrderEntity> orderPage = jpaOrderPage.map(OrderMapper::map);
    return PageDto.of(orderPage.getContent(), orderPage.getTotalElements());
  }

  @Override
  public Optional<OrderEntity> findById(Integer id) {
    return jpaOrderRepository.findByIdAndFetch(id)
        .map(OrderMapper::map);
  }

  @Override
  public void insert(OrderEntity orderEntity) {
    JpaOrder jpaOrder = OrderMapper.map(orderEntity);
    jpaOrderRepository.insert(jpaOrder);
    orderEntity.setId(jpaOrder.getId());
  }

  @Override
  public void update(OrderEntity orderEntity) {
    JpaOrder jpaOrder = OrderMapper.map(orderEntity);
    jpaOrderRepository.save(jpaOrder);
  }
}
