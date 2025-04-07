package org.atlas.domain.product.usecase.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Nullable;
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
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductInput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductOutput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductOutput.Product;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.search.SearchPort;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontSearchProductUseCaseHandler implements
    UseCaseHandler<SearchProductInput, SearchProductOutput> {

  private final @Nullable SearchPort searchPort;
  private final ProductRepository productRepository;

  @Override
  public SearchProductOutput handle(SearchProductInput input) throws Exception {
    PagingResult<ProductEntity> productEntityPage = null;
    if (searchPort != null) {
      // Using search engine
//      SearchCriteria criteria = ObjectMapperUtil.getInstance()
//          .map(input, SearchCriteria.class);
//      productEntityPage = searchPort.search(criteria, input.getPagingRequest());
    } else {
      // Using DB
      FindProductCriteria criteria = ObjectMapperUtil.getInstance()
          .map(input, FindProductCriteria.class);
      criteria.setStatus(ProductStatus.IN_STOCK);
      criteria.setIsActive(true);
      productEntityPage = productRepository.findByCriteria(criteria, input.getPagingRequest());
    }

    PagingResult<SearchProductOutput.Product> productPage = ObjectMapperUtil.getInstance()
        .mapPage(productEntityPage, SearchProductOutput.Product.class);
    return new SearchProductOutput(productPage.getResults(), productPage.getPagination());
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SearchProductInput {

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
  public static class SearchProductOutput extends PagingResult<Product> {

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
