package org.atlas.service.report.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Order implements Serializable {

    private Integer id;
    private User user;
    private List<OrderItem> orderItems;
    private BigDecimal amount;
    private Date createdAt;

    public void addOrderItem(OrderItem orderItem) {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        this.orderItems.add(orderItem);
    }
}
