package org.atlas.service.catalog.port.inbound.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.catalog.port.inbound.admin.ImportProductUseCase.Input;

public interface ImportProductUseCase extends UseCase<Input, Void> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    @NotNull
    private FileType fileType;

    @NotEmpty
    private byte[] fileContent;
  }
}
