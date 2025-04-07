package org.atlas.domain.product.usecase.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.usecase.admin.AdminDeleteProductUseCaseHandler.DeleteProductInput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.error.AppError;
import org.atlas.framework.event.contract.product.ProductDeletedEvent;
import org.atlas.framework.event.publisher.ProductEventPublisherPort;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminDeleteProductUseCaseHandler implements UseCaseHandler<DeleteProductInput, Void> {

  private final ProductRepository productRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final ProductEventPublisherPort productEventPublisherPort;

  @Override
  public Void handle(DeleteProductInput input) throws Exception {
    ProductEntity productEntity = productRepository.findById(input.getId())
        .orElseThrow(() -> new BusinessException(AppError.PRODUCT_NOT_FOUND));
    productRepository.delete(productEntity.getId());
    publishEvent(productEntity);
    return null;
  }

  private void publishEvent(ProductEntity productEntity) {
    ProductDeletedEvent event = new ProductDeletedEvent(applicationConfigPort.getApplicationName());
    event.setId(productEntity.getId());
    productEventPublisherPort.publish(event);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeleteProductInput {

    private Integer id;
  }
}
