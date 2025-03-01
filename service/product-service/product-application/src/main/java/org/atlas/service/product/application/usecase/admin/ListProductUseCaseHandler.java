package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.admin.ListProductUseCase;
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("adminListProductUseCaseHandler")
@RequiredArgsConstructor
public class ListProductUseCaseHandler implements ListProductUseCase {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    FindProductCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntities = productRepository.findByCriteria(criteria,
        input.getPagingRequest());
    PagingResult<Output.Product> products = ObjectMapperUtil.getInstance()
        .mapPage(productEntities, Output.Product.class);
    return new Output(products);
  }
}
