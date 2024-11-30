package org.atlas.service.order.contract.model;

import lombok.Data;
import org.atlas.service.product.contract.model.ProductDto;

@Data
public class OrderItemDto {

  private ProductDto product;
  private Integer quantity;
}
