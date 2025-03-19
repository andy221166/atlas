package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.enums.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.admin.DeleteProductUseCase;
import org.atlas.service.product.port.outbound.event.ProductEventPublisherPort;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCaseHandler implements DeleteProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;
  private final ApplicationConfigService applicationConfigService;
  private final ProductEventPublisherPort productEventPublisherPort;

  @Override
  @Transactional
  public Void handle(Input input) throws Exception {
    ProductEntity productEntity = productRepositoryPort.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    productRepositoryPort.delete(productEntity.getId());
    publishEvent(productEntity);
    return null;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductDeletedEvent event = new ProductDeletedEvent(
        applicationConfigService.getApplicationName());
    event.setId(productEntity.getId());
    productEventPublisherPort.publish(event);
  }
}
