package org.atlas.service.catalog.application.handler.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.cqrs.command.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateProductCommandHandler implements CommandHandler<CreateProductCommand, Integer> {

  private final ProductRepository productRepository;

  @Override
  public Integer handle(CreateProductCommand command) throws Exception {
    ProductEntity productEntity = ObjectMapperUtil.getInstance().map(command, ProductEntity.class);

    int inserted = productRepository.insert(productEntity);
    if (inserted == 0) {
      log.error("Failed to insert product: {}", productEntity);
      throw new BusinessException(AppError.INTERNAL_SERVER_ERROR);
    }
    return productEntity.getId();
  }
}
