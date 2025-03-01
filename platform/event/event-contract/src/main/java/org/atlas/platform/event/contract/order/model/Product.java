package org.atlas.platform.event.contract.order.model;

import lombok.Data;

@Data
public class Product {

  private Integer id;
  private String code;
  private String name;
  private String price;
}
