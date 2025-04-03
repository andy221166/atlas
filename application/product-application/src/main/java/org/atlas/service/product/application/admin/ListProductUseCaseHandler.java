package org.atlas.service.product.application.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.port.inbound.product.admin.ListProductUseCase;
import org.atlas.port.inbound.product.admin.ListProductUseCase.ListProductOutput.Product;
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("adminListProductUseCaseHandler")
@RequiredArgsConstructor
public class ListProductUseCaseHandler implements ListProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public ListProductOutput handle(ListProductInput input) throws Exception {
    FindProductCriteria params = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntityPage = productRepositoryPort.findByCriteria(params,
        input.getPagingRequest());
    PagingResult<Product> productPage = ObjectMapperUtil.getInstance()
        .mapPage(productEntityPage, Product.class);
    return new ListProductOutput(productPage.getResults(), productPage.getPagination());
  }
}
