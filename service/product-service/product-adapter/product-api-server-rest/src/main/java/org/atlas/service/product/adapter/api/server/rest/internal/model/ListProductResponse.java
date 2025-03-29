package org.atlas.service.product.adapter.api.server.rest.internal.model;

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
public class ListProductResponse {

  private List<Product> products;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Product {

    private Integer id;
    private String name;
    private BigDecimal price;
  }
}
