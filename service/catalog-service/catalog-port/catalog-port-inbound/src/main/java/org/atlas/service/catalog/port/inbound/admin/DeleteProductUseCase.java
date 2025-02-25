package org.atlas.service.catalog.port.inbound.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.usecase.port.UseCase;

public interface DeleteProductUseCase
    extends UseCase<DeleteProductUseCase.Input, Void> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer id;
  }
}
