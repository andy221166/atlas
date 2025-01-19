package org.atlas.service.user.application.handler.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateProductCommandHandler implements CommandHandler<UpdateProductCommand, Void> {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Void handle(UpdateProductCommand command) throws Exception {
    Product product = productRepository.findById(command.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    Category category = categoryRepository.findById(command.getCategoryId())
        .orElseThrow(() -> new BusinessException(AppError.CATEGORY_NOT_FOUND));

    // Merge command into entity
    ObjectMapperUtil.getInstance().merge(command, product);
    product.setCategory(category);

    int updated = productRepository.update(product);
    if (updated == 0) {
      log.error("Failed to update product: {}", product);
      throw new BusinessException(AppError.INTERNAL_SERVER_ERROR);
    }
    return null;
  }
}
