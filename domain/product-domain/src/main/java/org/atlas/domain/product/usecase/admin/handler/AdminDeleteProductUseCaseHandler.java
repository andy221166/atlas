package org.atlas.domain.product.usecase.admin.handler;

import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.port.messaging.ProductMessagePublisherPort;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.service.ProductImageService;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.domain.event.contract.product.ProductDeletedEvent;
import org.atlas.framework.domain.exception.DomainException;
import org.atlas.framework.error.AppError;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminDeleteProductUseCaseHandler implements UseCaseHandler<Integer, Void> {

  private final ProductRepository productRepository;
  private final ProductImageService productImageService;
  private final ApplicationConfigPort applicationConfigPort;
  private final ProductMessagePublisherPort productMessagePublisherPort;

  @Override
  public Void handle(Integer productId) throws Exception {
    // Delete product from DB
    ProductEntity productEntity = productRepository.findById(productId)
        .orElseThrow(() -> new DomainException(AppError.PRODUCT_NOT_FOUND));
    productRepository.delete(productEntity.getId());

    // Delete image
    productImageService.deleteImage(productEntity.getId());

    // Publish event
    publishEvent(productEntity);

    return null;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductDeletedEvent event = new ProductDeletedEvent(applicationConfigPort.getApplicationName());
    event.setProductId(productEntity.getId());
    productMessagePublisherPort.publish(event);
  }
}
