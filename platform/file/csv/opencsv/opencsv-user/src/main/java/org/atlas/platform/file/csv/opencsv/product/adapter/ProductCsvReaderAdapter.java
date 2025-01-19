package org.atlas.platform.file.csv.opencsv.product.adapter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import org.atlas.platform.file.csv.opencsv.core.OpenCsvReader;
import org.atlas.service.product.contract.file.csv.ProductCsvReader;
import org.atlas.service.product.domain.ImportProductItem;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvReaderAdapter implements ProductCsvReader {

  @Override
  public void read(byte[] fileContent, int chunkSize, Consumer<List<ImportProductItem>> chunkConsumer)
      throws IOException {
    OpenCsvReader.read(fileContent, ImportProductItem.class, chunkSize, chunkConsumer);
  }
}
