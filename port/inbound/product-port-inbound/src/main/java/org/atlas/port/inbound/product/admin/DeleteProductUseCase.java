package org.atlas.port.inbound.product.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.port.inbound.product.admin.DeleteProductUseCase.DeleteProductInput;

public interface DeleteProductUseCase extends UseCase<DeleteProductInput, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteProductInput {

    private Integer id;
  }
}
