package org.atlas.domain.product.usecase.front;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.service.ProductImageService;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.ProductOutput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductInput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.search.SearchPort;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontSearchProductUseCaseHandler implements
    UseCaseHandler<SearchProductInput, PagingResult<ProductOutput>> {

  private final @Nullable SearchPort searchPort;
  private final ProductRepository productRepository;
  private final ProductImageService productImageService;

  @Override
  public PagingResult<ProductOutput> handle(SearchProductInput input) throws Exception {
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

    if (productEntityPage == null) {
      return PagingResult.empty();
    }

    List<ProductOutput> productOutputs = productEntityPage.getData()
        .stream()
        .map(productEntity -> {
          ProductOutput productOutput = ObjectMapperUtil.getInstance()
              .map(productEntity, ProductOutput.class);
          productOutput.setImage(productImageService.getImage(productEntity.getId()));
          return productOutput;
        })
        .toList();

    return PagingResult.of(productOutputs, productEntityPage.getPagination());
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SearchProductInput {

    private String keyword;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer brandId;
    private List<Integer> categoryIds;
    @Valid
    private PagingRequest pagingRequest;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductOutput {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String image;
  }
}
