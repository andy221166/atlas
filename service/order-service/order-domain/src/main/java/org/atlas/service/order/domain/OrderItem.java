package org.atlas.service.order.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItem implements Serializable {

  private Integer orderId;
  private Integer productId;
  private BigDecimal productPrice;
  private Integer quantity;
}
