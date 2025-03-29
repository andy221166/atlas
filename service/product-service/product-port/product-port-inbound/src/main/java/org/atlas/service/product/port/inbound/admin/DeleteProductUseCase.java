package org.atlas.service.product.port.inbound.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.admin.DeleteProductUseCase.DeleteProductInput;

public interface DeleteProductUseCase extends UseCase<DeleteProductInput, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteProductInput {

    private Integer id;
  }
}
