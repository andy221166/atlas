package org.atlas.platform.persistence.jpa.order.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.commons.util.paging.PagingRequest;
import org.atlas.platform.persistence.jpa.order.entity.JpaOrder;
import org.atlas.platform.persistence.jpa.order.mapper.OrderMapper;
import org.atlas.platform.persistence.jpa.order.repository.JpaOrderRepository;
import org.atlas.service.order.contract.repository.OrderRepository;
import org.atlas.service.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public PageDto<Order> findByUserId(Integer userId, PagingRequest pagingRequest) {
        Pageable pageable = PageRequest.of(pagingRequest.getPage(), pagingRequest.getSize());
        Page<JpaOrder> jpaOrderPage = jpaOrderRepository.findByUserId(userId, pageable);
        Page<Order> orderPage = jpaOrderPage.map(OrderMapper::map);
        return PageDto.of(orderPage.getContent(), orderPage.getTotalElements());
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return jpaOrderRepository.findByIdAndFetch(id)
            .map(OrderMapper::map);
    }

    @Override
    public void insert(Order order) {
        JpaOrder jpaOrder = OrderMapper.map(order, false);
        jpaOrderRepository.insert(jpaOrder);
        order.setId(jpaOrder.getId());
    }

    @Override
    public void update(Order order) {
        JpaOrder jpaOrder = OrderMapper.map(order, true);
        jpaOrderRepository.save(jpaOrder);
    }
}
