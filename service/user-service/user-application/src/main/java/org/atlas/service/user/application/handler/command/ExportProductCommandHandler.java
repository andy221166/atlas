package org.atlas.service.user.application.handler.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.auth.application.Constant;
import org.atlas.service.product.contract.file.csv.ProductCsvWriter;
import org.atlas.service.product.contract.file.excel.ProductExcelWriter;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExportProductCommandHandler implements CommandHandler<ExportProductCommand, byte[]> {

  private final ProductRepository productRepository;
  private final ProductCsvWriter productCsvWriter;
  private final ProductExcelWriter productExcelWriter;

  @Override
  public byte[] handle(ExportProductCommand command) throws Exception {
    List<Product> products = productRepository.findAll();
    switch (command.getFileType()) {
      case CSV -> {
        return productCsvWriter.write(products);
      }
      case EXCEL -> {
        return productExcelWriter.write(products, org.atlas.service.user.application.Constant.EXCEL_SHEET_NAME);
      }
      default ->
          throw new IllegalArgumentException("Unsupported file type: " + command.getFileType());
    }
  }
}
