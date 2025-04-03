package org.atlas.port.inbound.product.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.port.inbound.product.admin.ImportProductUseCase.ImportProductInput;

public interface ImportProductUseCase extends UseCase<ImportProductInput, Void> {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class ImportProductInput {

    @NotNull
    private FileType fileType;

    @NotEmpty
    private byte[] fileContent;
  }
}
