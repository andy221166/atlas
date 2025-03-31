package org.atlas.service.product.port.inbound.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.paging.Pagination;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.front.SearchProductUseCase.SearchProductInput;
import org.atlas.service.product.port.inbound.front.SearchProductUseCase.SearchProductOutput;
import org.atlas.service.product.port.inbound.front.SearchProductUseCase.SearchProductOutput.Product;

public interface SearchProductUseCase
    extends UseCase<SearchProductInput, SearchProductOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class SearchProductInput {

    private String keyword;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Integer> brandIds;
    private List<Integer> categoryIds;
    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  class SearchProductOutput extends PagingResult<Product> {

    public SearchProductOutput(List<Product> results, Pagination pagination) {
      super(results, pagination);
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
