package org.atlas.service.report.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {

  private Integer orderId;
  private Product product;
  private Integer quantity;
}