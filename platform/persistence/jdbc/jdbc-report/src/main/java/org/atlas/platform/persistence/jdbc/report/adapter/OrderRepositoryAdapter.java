package org.atlas.platform.persistence.jdbc.report.adapter;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.report.repository.JdbcOrderRepository;
import org.atlas.service.report.contract.repository.OrderRepository;
import org.atlas.service.report.domain.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JdbcOrderRepository jdbcOrderRepository;

    @Override
    public void insert(Order order) {
       jdbcOrderRepository.insert(order);
    }
}
