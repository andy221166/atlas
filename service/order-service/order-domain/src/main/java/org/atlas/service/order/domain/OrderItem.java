package org.atlas.service.order.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItem implements Serializable {

    private Integer orderId;
    private Integer productId;
    private BigDecimal productPrice;
    private Integer quantity;
}
