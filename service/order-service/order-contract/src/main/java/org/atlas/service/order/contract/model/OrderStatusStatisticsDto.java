package org.atlas.service.order.contract.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusStatisticsDto {

    private OrderStatus status;
    private long totalOrders;
    private BigDecimal totalAmount;
}
