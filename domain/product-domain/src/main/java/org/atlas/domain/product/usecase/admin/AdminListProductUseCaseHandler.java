package org.atlas.domain.product.usecase.admin;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ListProductInput;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ListProductOutput;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ListProductOutput.Product;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListProductUseCaseHandler implements UseCaseHandler<ListProductInput, ListProductOutput> {

  private final ProductRepository productRepository;

  @Override
  public ListProductOutput handle(ListProductInput input) throws Exception {
    FindProductCriteria params = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntityPage = productRepository.findByCriteria(params,
        input.getPagingRequest());
    PagingResult<Product> productPage = ObjectMapperUtil.getInstance()
        .mapPage(productEntityPage, Product.class);
    return new ListProductOutput(productPage.getResults(), productPage.getPagination());
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListProductInput {

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
  public static class ListProductOutput extends PagingResult<Product> {

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
