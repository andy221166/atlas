package org.atlas.framework.event.contract.order.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Product {

  private Integer id;
  private String name;
  private BigDecimal price;
}
