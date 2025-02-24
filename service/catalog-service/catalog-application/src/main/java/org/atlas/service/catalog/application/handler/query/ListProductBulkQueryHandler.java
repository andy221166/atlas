package org.atlas.service.catalog.application.handler.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.query.QueryHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.ListProductBulkQuery;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListProductBulkQueryHandler implements
    QueryHandler<ListProductBulkQuery, List<ProductDto>> {

  private final ProductRepository productRepository;

  @Override
  @Transactional(readOnly = true)
  public List<ProductDto> handle(ListProductBulkQuery command) throws Exception {
    List<ProductEntity> productEntities = productRepository.findByIdIn(command.getIds());
    return productEntities.stream()
        .map(product -> ObjectMapperUtil.getInstance().map(product, ProductDto.class))
        .toList();
  }
}
