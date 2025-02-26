package org.atlas.service.catalog.port.inbound.admin;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.usecase.port.UseCase;
import org.atlas.service.catalog.domain.entity.ProductStatus;

public interface ExportProductUseCase
    extends UseCase<ExportProductUseCase.Input, ExportProductUseCase.Output> {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Input {

    private Integer id;
    private String keyword;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
    private Integer brandId;
    private List<Integer> categoryIds;

    @NotNull
    private FileType fileType;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class Output {

    private byte[] fileContent;
  }
}
