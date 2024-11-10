package org.atlas.service.order.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.commons.model.Auditable;
import org.atlas.service.order.domain.shared.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Order extends Auditable implements Serializable {

    @EqualsAndHashCode.Include
    private Integer id;
    private Integer userId;
    private List<OrderItem> orderItems;
    private BigDecimal amount;
    private OrderStatus status;
    private String canceledReason;

    public void addOrderItem(OrderItem orderItem) {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
    }
}
