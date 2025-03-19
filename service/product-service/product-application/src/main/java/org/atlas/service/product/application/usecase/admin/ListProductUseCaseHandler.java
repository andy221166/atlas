package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.admin.ListProductUseCase;
import org.atlas.service.product.port.outbound.repository.FindProductParams;
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
    FindProductParams criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductParams.class);
    PagingResult<ProductEntity> productEntities = productRepositoryPort.findByCriteria(criteria,
        input.getPagingRequest());
    PagingResult<Output.Product> products = ObjectMapperUtil.getInstance()
        .mapPage(productEntities, Output.Product.class);
    return new Output(products);
  }
}
