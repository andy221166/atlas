package org.atlas.framework.event.contract.order.model;

import lombok.Data;

@Data
public class OrderItem {

  private Product product;
  private Integer quantity;
}
