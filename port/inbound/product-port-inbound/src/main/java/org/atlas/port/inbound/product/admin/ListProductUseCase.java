package org.atlas.port.inbound.product.admin;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
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
import org.atlas.domain.product.entity.ProductStatus;
import org.atlas.port.inbound.product.admin.ListProductUseCase.ListProductInput;
import org.atlas.port.inbound.product.admin.ListProductUseCase.ListProductOutput;
import org.atlas.port.inbound.product.admin.ListProductUseCase.ListProductOutput.Product;

public interface ListProductUseCase extends UseCase<ListProductInput, ListProductOutput> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ListProductInput {

    private Integer id;
    private String keyword;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
    private Integer brandId;
    private List<Integer> categoryIds;
    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  class ListProductOutput extends PagingResult<Product> {

    public ListProductOutput(List<Product> results, Pagination pagination) {
      super(results, pagination);
    }

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
}
