package org.atlas.service.product.application.usecase.admin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.port.inbound.usecase.admin.ExportProductUseCase;
import org.atlas.service.product.port.outbound.file.csv.ProductCsvWriter;
import org.atlas.service.product.port.outbound.file.excel.ProductExcelWriter;
import org.atlas.service.product.port.outbound.file.model.write.ProductRow;
import org.atlas.service.product.port.outbound.repository.FindProductCriteria;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExportProductUseCaseHandler implements ExportProductUseCase {

  private final ProductRepository productRepository;
  private final ProductCsvWriter productCsvWriter;
  private final ProductExcelWriter productExcelWriter;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Input input) throws Exception {
    FindProductCriteria criteria = ObjectMapperUtil.getInstance()
        .map(input, FindProductCriteria.class);
    PagingResult<ProductEntity> productEntities = productRepository.findByCriteria(criteria,
        PagingRequest.unpaged());
    List<ProductRow> productRows = ObjectMapperUtil.getInstance().mapList(
        productEntities.getResults(), ProductRow.class);

    byte[] fileContent;
    switch (input.getFileType()) {
      case CSV -> fileContent = productCsvWriter.write(productRows);
      case EXCEL -> fileContent = productExcelWriter.write(productRows);
      default -> throw new UnsupportedOperationException(
          "Unsupported file type: " + input.getFileType());
    }
    return new Output(fileContent);
  }
}
