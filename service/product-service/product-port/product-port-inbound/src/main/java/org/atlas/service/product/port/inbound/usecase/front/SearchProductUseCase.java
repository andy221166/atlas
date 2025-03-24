package org.atlas.service.product.port.inbound.usecase.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.usecase.front.SearchProductUseCase.Output.Product;

public interface SearchProductUseCase
    extends UseCase<SearchProductUseCase.Input, SearchProductUseCase.Output> {

  @Data
  @Builder
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
  @EqualsAndHashCode(callSuper = false)
  class Output extends PagingResult<Product> {

    public Output(List<Product> results, long totalCount) {
      super(results, totalCount);
    }

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
}
