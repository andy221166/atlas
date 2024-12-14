package org.atlas.service.product.application.handler.command;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.commons.enums.FileType;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.product.contract.command.ImportProductCommand;
import org.atlas.service.product.contract.csv.ProductCsvReader;
import org.atlas.service.product.contract.excel.ProductExcelReader;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductCommandHandler implements CommandHandler<ImportProductCommand, Integer> {

  private final ProductCsvReader csvReader;
  private final ProductExcelReader excelReader;
  private final ProductRepository productRepository;

  @Override
  public Integer handle(ImportProductCommand command) throws Exception {
    List<Product> products;
    try {
      products = readProducts(command.getFileType(), command.getFileContent());
      log.info("Read {} products from file", CollectionUtils.size(products));
    } catch (Exception e) {
      throw new IOException("Failed to read products");
    }

    if (CollectionUtils.isEmpty(products)) {
      log.warn("Empty products");
      return 0;
    }

    int imported = productRepository.insertBatch(products);
    log.info("Imported {} products from file", imported);
    return imported;
  }

  private List<Product> readProducts(FileType fileType, byte[] fileContent) throws Exception {
    switch (fileType) {
      case CSV -> {
        return csvReader.read(fileContent);
      }
      case EXCEL -> {
        return excelReader.read(fileContent);
      }
      default -> throw new UnsupportedOperationException("Unsupported file type");
    }
  }
}
