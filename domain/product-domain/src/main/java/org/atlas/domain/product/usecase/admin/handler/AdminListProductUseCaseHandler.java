package org.atlas.domain.product.usecase.admin.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.service.ProductImageService;
import org.atlas.domain.product.usecase.admin.model.AdminListProductInput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminListProductUseCaseHandler implements
    UseCaseHandler<AdminListProductInput, PagingResult<ProductEntity>> {

  private final ProductRepository productRepository;
  private final ProductImageService productImageService;

  @Override
  public PagingResult<ProductEntity> handle(AdminListProductInput input) throws Exception {
    FindProductCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntityPage = productRepository.findByCriteria(criteria,
        input.getPagingRequest());

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
