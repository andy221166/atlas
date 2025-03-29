package org.atlas.service.product.application.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.admin.ExportProductUseCase;
import org.atlas.service.product.port.outbound.file.csv.ProductCsvWriterPort;
import org.atlas.service.product.port.outbound.file.excel.ProductExcelWriterPort;
import org.atlas.service.product.port.outbound.file.model.write.ProductRow;
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExportProductUseCaseHandler implements ExportProductUseCase {

  private final ProductRepositoryPort productRepositoryPort;
  private final ProductCsvWriterPort productCsvWriterPort;
  private final ProductExcelWriterPort productExcelWriterPort;

  @Override
  @Transactional(readOnly = true)
  public ExportProductOutput handle(ExportProductInput input) throws Exception {
    FindProductCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntities = productRepositoryPort.findByCriteria(criteria,
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
}
