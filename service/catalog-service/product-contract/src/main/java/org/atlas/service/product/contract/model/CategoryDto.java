package org.atlas.service.product.contract.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class CategoryDto implements Serializable {

  private Integer id;
  private String name;
}