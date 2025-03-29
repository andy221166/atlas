package org.atlas.service.product.adapter.api.server.rest.admin.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.service.product.adapter.api.server.rest.admin.model.ListProductResponse.Product;
import org.atlas.service.product.domain.entity.ProductStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListProductResponse extends PagingResult<Product> {

  @Data
  public static class Product {

    private Integer id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
  }
}
