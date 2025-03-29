package org.atlas.service.product.adapter.api.server.rest.front.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.product.adapter.api.server.rest.front.model.SearchProductResponse.Product;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchProductResponse extends PagingResult<Product> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Product {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
  }
}
