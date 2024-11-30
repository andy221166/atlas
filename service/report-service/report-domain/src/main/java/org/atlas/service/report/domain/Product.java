package org.atlas.service.report.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class Product implements Serializable {

  private Integer id;
  private String name;
  private BigDecimal price;
}
