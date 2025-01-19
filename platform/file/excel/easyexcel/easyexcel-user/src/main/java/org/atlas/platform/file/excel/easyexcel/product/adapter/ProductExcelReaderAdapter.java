package org.atlas.platform.file.excel.easyexcel.product.adapter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import org.atlas.platform.file.excel.easyexcel.core.EasyExcelReader;
import org.atlas.service.product.contract.file.excel.ProductExcelReader;
import org.atlas.service.product.domain.ImportProductItem;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReader {

  @Override
  public void read(byte[] fileContent, String sheetName, int chunkSize, Consumer<List<ImportProductItem>> chunkConsumer)
      throws IOException {
    EasyExcelReader.read(fileContent, sheetName, ImportProductItem.class, chunkSize, chunkConsumer);
  }
}
