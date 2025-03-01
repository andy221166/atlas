package org.atlas.service.product.application.usecase.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("internalListProductUseCaseHandler")
@RequiredArgsConstructor
public class ListProductUseCaseHandler implements ListProductUseCase {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    List<ProductEntity> productEntities = productRepository.findByIdIn(input.getIds());
    List<Output.Product> products = productEntities.stream()
        .map(product -> ObjectMapperUtil.getInstance().map(product, Output.Product.class))
        .toList();
    return new Output(products);
  }
}
