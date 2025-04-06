package org.atlas.domain.product.shared.internal;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListProductOutput {

  private List<Product> products;

  @Data
  public static class Product {

    private Integer id;
    private String name;
    private BigDecimal price;
  }
}
