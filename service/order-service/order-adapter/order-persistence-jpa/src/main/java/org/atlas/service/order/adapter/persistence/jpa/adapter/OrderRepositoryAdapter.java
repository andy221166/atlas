package org.atlas.service.order.adapter.persistence.jpa.adapter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.order.adapter.persistence.jpa.entity.JpaOrderEntity;
import org.atlas.service.order.adapter.persistence.jpa.repository.JpaOrderRepository;
import org.atlas.service.order.domain.entity.OrderEntity;
import org.atlas.service.order.port.outbound.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;

  @Override
  public PagingResult<OrderEntity> findAll(PagingRequest pagingRequest) {
    Pageable pageable = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize());
    Page<JpaOrderEntity> jpaOrderEntityPage = jpaOrderRepository.findAll(pageable);
    Page<OrderEntity> orderEntityPage = jpaOrderEntityPage.map(jpaOrderEntity ->
        ObjectMapperUtil.getInstance().map(jpaOrderEntity, OrderEntity.class));
    return PagingResult.of(orderEntityPage.getContent(), orderEntityPage.getTotalElements());
  }

  @Override
  public PagingResult<OrderEntity> findByUserId(Integer userId, PagingRequest pagingRequest) {
    Pageable pageable = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize());
    Page<JpaOrderEntity> jpaOrderEntityPage = jpaOrderRepository.findByUserId(userId, pageable);
    Page<OrderEntity> orderEntityPage = jpaOrderEntityPage.map(jpaOrderEntity ->
        ObjectMapperUtil.getInstance().map(jpaOrderEntity, OrderEntity.class));
    return PagingResult.of(orderEntityPage.getContent(), orderEntityPage.getTotalElements());
  }

  @Override
  public Optional<OrderEntity> findById(Integer id) {
    return jpaOrderRepository.findByIdAndFetch(id)
        .map(jpaOrderEntity ->
            ObjectMapperUtil.getInstance().map(jpaOrderEntity, OrderEntity.class));
  }

  @Override
  public void insert(OrderEntity orderEntity) {
    JpaOrderEntity jpaOrder = ObjectMapperUtil.getInstance().map(orderEntity, JpaOrderEntity.class);
    jpaOrderRepository.insert(jpaOrder);
    orderEntity.setId(jpaOrder.getId());
  }

  @Override
  public void update(OrderEntity orderEntity) {
    JpaOrderEntity jpaOrder = ObjectMapperUtil.getInstance().map(orderEntity, JpaOrderEntity.class);
    jpaOrderRepository.save(jpaOrder);
  }
}
