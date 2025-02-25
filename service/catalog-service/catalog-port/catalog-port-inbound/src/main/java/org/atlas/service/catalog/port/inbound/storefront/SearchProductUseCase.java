package org.atlas.service.catalog.port.inbound.storefront;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.catalog.domain.entity.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public interface SearchProductUseCase
    extends UseCase<SearchProductUseCase.Input, SearchProductUseCase.Output> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private String keyword;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer brandId;
    private List<Integer> categoryIds;
    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private PagingResult<Product> products;

    @Data
    public static class Product {

      private Integer id;
      private String name;
      private BigDecimal price;
      private ProductStatus status;
      private String description;
      private String imageUrl;
    }
  }
}
