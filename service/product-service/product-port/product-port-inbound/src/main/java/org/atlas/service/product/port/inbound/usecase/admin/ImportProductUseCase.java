package org.atlas.service.product.port.inbound.usecase.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.product.port.inbound.usecase.admin.ImportProductUseCase.Input;

public interface ImportProductUseCase extends UseCase<Input, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    @NotNull
    private FileType fileType;

    @NotEmpty
    private byte[] fileContent;
  }
}
