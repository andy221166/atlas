package org.atlas.platform.persistence.jpa.report.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jpa.report.entity.JpaOrder;
import org.atlas.platform.persistence.jpa.report.mapper.OrderMapper;
import org.atlas.platform.persistence.jpa.report.repository.JpaOrderRepository;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public void insert(Order order) {
        JpaOrder jpaOrder = OrderMapper.map(order);
        jpaOrderRepository.insert(jpaOrder);
        order.setId(jpaOrder.getId());
    }
}
