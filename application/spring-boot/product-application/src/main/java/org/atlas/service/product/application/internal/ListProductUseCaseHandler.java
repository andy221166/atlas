package org.atlas.service.product.application.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.port.inbound.product.internal.ListProductUseCase;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductOutput.Product;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("internalListProductUseCaseHandler")
@RequiredArgsConstructor
public class ListProductUseCaseHandler implements ListProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public ListProductOutput handle(ListProductInput input) throws Exception {
    List<ProductEntity> productEntities = productRepositoryPort.findByIdIn(input.getIds());
    List<Product> products = ObjectMapperUtil.getInstance()
        .mapList(productEntities, Product.class);
    return new ListProductOutput(products);
  }
}
