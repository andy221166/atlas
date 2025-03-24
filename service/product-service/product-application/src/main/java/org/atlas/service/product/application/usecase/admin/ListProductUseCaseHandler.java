package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.admin.ListProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.ListProductUseCase.Output.Product;
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
  public Output handle(Input input) throws Exception {
    FindProductCriteria params = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntityPage = productRepositoryPort.findByCriteria(params,
        input.getPagingRequest());
    PagingResult<Product> productPage = ObjectMapperUtil.getInstance()
        .mapPage(productEntityPage, Product.class);
    return new Output(productPage);
  }
}
