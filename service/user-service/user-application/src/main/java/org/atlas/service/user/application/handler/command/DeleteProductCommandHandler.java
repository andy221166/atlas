package org.atlas.service.user.application.handler.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteProductCommandHandler implements CommandHandler<DeleteProductCommand, Void> {

  private final ProductRepository productRepository;

  @Override
  public Void handle(DeleteProductCommand command) throws Exception {
    productRepository.findById(command.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));

    int deleted = productRepository.deleteById(command.getId());
    if (deleted == 0) {
      log.error("Failed to delete product: {}", command.getId());
      throw new BusinessException(AppError.INTERNAL_SERVER_ERROR);
    }
    return null;
  }
}
