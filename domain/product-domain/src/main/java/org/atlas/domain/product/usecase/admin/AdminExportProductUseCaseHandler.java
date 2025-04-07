package org.atlas.domain.product.usecase.admin;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.FindProductCriteria;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.admin.AdminExportProductUseCaseHandler.ExportProductInput;
import org.atlas.domain.product.usecase.admin.AdminExportProductUseCaseHandler.ExportProductOutput;
import org.atlas.framework.file.csv.ProductCsvWriterPort;
import org.atlas.framework.file.enums.FileType;
import org.atlas.framework.file.excel.ProductExcelWriterPort;
import org.atlas.framework.file.model.write.ProductRow;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class AdminExportProductUseCaseHandler implements
    UseCaseHandler<ExportProductInput, ExportProductOutput> {

  private final ProductRepository productRepository;
  private final ProductCsvWriterPort productCsvWriterPort;
  private final ProductExcelWriterPort productExcelWriterPort;

  @Override
  public ExportProductOutput handle(ExportProductInput input) throws Exception {
    FindProductCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntities = productRepository.findByCriteria(criteria,
        PagingRequest.unpaged());
    List<ProductRow> productRows = ObjectMapperUtil.getInstance().mapList(
        productEntities.getResults(), ProductRow.class);

    byte[] fileContent;
    switch (input.getFileType()) {
      case CSV -> fileContent = productCsvWriterPort.write(productRows);
      case EXCEL -> fileContent = productExcelWriterPort.write(productRows);
      default -> throw new UnsupportedOperationException(
          "Unsupported file type: " + input.getFileType());
    }
    return new ExportProductOutput(fileContent);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ExportProductInput {

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
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ExportProductOutput {

    private byte[] fileContent;
  }
}
