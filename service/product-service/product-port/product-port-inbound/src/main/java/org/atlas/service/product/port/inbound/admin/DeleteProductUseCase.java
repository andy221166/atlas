package org.atlas.service.product.port.inbound.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;

public interface DeleteProductUseCase
    extends UseCase<DeleteProductUseCase.Input, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer id;
  }
}
