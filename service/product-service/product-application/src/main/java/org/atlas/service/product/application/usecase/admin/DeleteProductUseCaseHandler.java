package org.atlas.service.product.application.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.service.product.port.inbound.admin.DeleteProductUseCase;
import org.atlas.service.product.port.outbound.event.publisher.ProductEventPublisher;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCaseHandler implements DeleteProductUseCase {

  private final ProductRepository productRepository;
  private final ProductEventPublisher productEventPublisher;

  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  @Transactional
  public Void handle(Input input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    productRepository.delete(productEntity);
    publishEvent(productEntity);
    return null;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductDeletedEvent event = new ProductDeletedEvent(applicationName);
    event.setId(productEntity.getId());
    productEventPublisher.publish(event);
  }
}
