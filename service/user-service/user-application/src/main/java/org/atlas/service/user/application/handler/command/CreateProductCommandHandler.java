package org.atlas.service.user.application.handler.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateProductCommandHandler implements CommandHandler<CreateProductCommand, Integer> {

  private final ProductRepository productRepository;

  @Override
  public Integer handle(CreateProductCommand command) throws Exception {
    Product product = ObjectMapperUtil.getInstance().map(command, Product.class);

    int inserted = productRepository.insert(product);
    if (inserted == 0) {
      log.error("Failed to insert product: {}", product);
      throw new BusinessException(AppError.INTERNAL_SERVER_ERROR);
    }
    return product.getId();
  }
}
