package org.atlas.domain.product.usecase.front.handler;

import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.service.ProductImageService;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.front.model.FrontSearchProductInput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.search.SearchPort;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class FrontSearchProductUseCaseHandler implements
    UseCaseHandler<FrontSearchProductInput, PagingResult<ProductEntity>> {

  private final @Nullable SearchPort searchPort;
  private final ProductRepository productRepository;
  private final ProductImageService productImageService;

  @Override
  public PagingResult<ProductEntity> handle(FrontSearchProductInput input) throws Exception {
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

    // Set image
    productEntityPage.getData()
        .forEach(product ->
            product.setImage(productImageService.getImage(product.getId())));

    return productEntityPage;
  }
}
