package org.atlas.platform.persistence.mybatis.report.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.report.mapper.OrderMapper;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.atlas.service.report.domain.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderMapper orderMapper;

    @Override
    public void insert(Order order) {
        // Insert the order and get the generated ID
        orderMapper.insertOrder(order);

        // Insert each order item
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrderId(order.getId());
            orderMapper.insertOrderItem(orderItem);
        }
    }
}
