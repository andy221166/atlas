package org.atlas.infrastructure.persistence.jpa.adapter.order;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.order.entity.OrderEntity;
import org.atlas.domain.order.repository.OrderRepository;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.infrastructure.persistence.jpa.adapter.order.entity.JpaOrderEntity;
import org.atlas.infrastructure.persistence.jpa.adapter.order.repository.JpaOrderRepository;
import org.atlas.infrastructure.persistence.jpa.core.paging.PagingConverter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;

  @Override
  public PagingResult<OrderEntity> findAll(PagingRequest pagingRequest) {
    Pageable pageable = PagingConverter.convert(pagingRequest);
    PagingResult<JpaOrderEntity> jpaOrderEntityPage = PagingConverter.convert(
        jpaOrderRepository.findAll(pageable));
    return ObjectMapperUtil.getInstance()
        .mapPage(jpaOrderEntityPage, OrderEntity.class);
  }

  @Override
  public PagingResult<OrderEntity> findByUserId(Integer userId, PagingRequest pagingRequest) {
    Pageable pageable = PagingConverter.convert(pagingRequest);
    PagingResult<JpaOrderEntity> jpaOrderEntityPage = PagingConverter.convert(
        jpaOrderRepository.findByUserId(userId, pageable));
    return ObjectMapperUtil.getInstance()
        .mapPage(jpaOrderEntityPage, OrderEntity.class);
  }

  @Override
  public Optional<OrderEntity> findById(Integer id) {
    return jpaOrderRepository.findByIdAndFetch(id)
        .map(jpaOrderEntity ->
            ObjectMapperUtil.getInstance()
                .map(jpaOrderEntity, OrderEntity.class));
  }

  @Override
  public void insert(OrderEntity orderEntity) {
    JpaOrderEntity jpaOrder = ObjectMapperUtil.getInstance().map(orderEntity, JpaOrderEntity.class);

    // To avoid null order_id
    jpaOrder.getOrderItems().forEach(jpaOrderItem -> jpaOrderItem.setOrder(jpaOrder));

    jpaOrderRepository.insert(jpaOrder);
    orderEntity.setId(jpaOrder.getId());
  }

  @Override
  public void update(OrderEntity orderEntity) {
    JpaOrderEntity jpaOrder = ObjectMapperUtil.getInstance()
        .map(orderEntity, JpaOrderEntity.class);
    jpaOrderRepository.save(jpaOrder);
  }
}
