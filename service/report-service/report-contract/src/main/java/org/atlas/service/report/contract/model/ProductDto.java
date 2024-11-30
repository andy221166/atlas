package org.atlas.service.report.contract.model;

import lombok.Data;

@Data
public class ProductDto {

  private Integer id;
  private String name;
  private Integer totalQuantity;
}