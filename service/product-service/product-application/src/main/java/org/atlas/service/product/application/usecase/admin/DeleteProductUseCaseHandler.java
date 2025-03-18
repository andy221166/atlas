package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.admin.DeleteProductUseCase;
import org.atlas.service.product.port.outbound.event.ProductEventPublisher;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCaseHandler implements DeleteProductUseCase {

  private final ProductRepository productRepository;
  private final ApplicationConfigService applicationConfigService;
  private final ProductEventPublisher productEventPublisher;

  @Override
  @Transactional
  public Void handle(Input input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    productRepository.delete(productEntity.getId());
    publishEvent(productEntity);
    return null;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductDeletedEvent event = new ProductDeletedEvent(
        applicationConfigService.getApplicationName());
    event.setId(productEntity.getId());
    productEventPublisher.publish(event);
  }
}
