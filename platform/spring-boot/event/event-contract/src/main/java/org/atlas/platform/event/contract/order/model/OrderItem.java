package org.atlas.platform.event.contract.order.model;

import lombok.Data;

@Data
public class OrderItem {

  private Product product;
  private Integer quantity;
}
