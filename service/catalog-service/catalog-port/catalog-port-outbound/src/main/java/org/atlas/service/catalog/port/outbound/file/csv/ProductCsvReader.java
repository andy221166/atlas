package org.atlas.service.catalog.port.outbound.file.csv;

import org.atlas.service.catalog.port.outbound.file.model.ImportProductItem;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface ProductCsvReader {

  List<ImportProductItem> read(byte[] fileContent, int chunkSize, Consumer<List<ImportProductItem>> chunkConsumer) throws IOException;
}
