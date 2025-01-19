package org.atlas.platform.event.contract.order.payload;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemPayload {

  private Integer productId;
  private String productName;
  private BigDecimal productPrice;
  private Integer quantity;
}
