package org.atlas.service.catalog.application.handler.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.cqrs.command.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.catalog.domain.entity.CategoryEntity;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateProductCommandHandler implements CommandHandler<UpdateProductCommand, Void> {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Void handle(UpdateProductCommand command) throws Exception {
    ProductEntity productEntity = productRepository.findById(command.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    CategoryEntity categoryEntity = categoryRepository.findById(command.getCategoryId())
        .orElseThrow(() -> new BusinessException(AppError.CATEGORY_NOT_FOUND));

    // Merge command into entity
    ObjectMapperUtil.getInstance().merge(command, productEntity);
    productEntity.setCategory(categoryEntity);

    int updated = productRepository.update(productEntity);
    if (updated == 0) {
      log.error("Failed to update product: {}", productEntity);
      throw new BusinessException(AppError.INTERNAL_SERVER_ERROR);
    }
    return null;
  }
}
