package org.atlas.domain.product.usecase.admin;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ListProductInput;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ProductOutput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListProductUseCaseHandler implements
    UseCaseHandler<ListProductInput, PagingResult<ProductOutput>> {

  private final ProductRepository productRepository;
  private final ProductImageService productImageService;

  @Override
  public PagingResult<ProductOutput> handle(ListProductInput input) throws Exception {
    FindProductCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntityPage = productRepository.findByCriteria(criteria,
        input.getPagingRequest());

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
  public static class ProductOutput {

    private Integer id;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer quantity;
    private ProductStatus status;
  }
}
