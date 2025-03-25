package org.atlas.service.product.port.inbound.usecase.front;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.checkerframework.checker.index.qual.Positive;

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
    private List<Integer> brandIds;
    private List<Integer> categoryIds;
    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private PagingResult<Product> productPage;

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
