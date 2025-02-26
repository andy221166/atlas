package org.atlas.service.catalog.port.outbound.file.excel;

import org.atlas.service.catalog.port.outbound.file.model.ImportProductItem;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface ProductExcelReader {

  String EXCEL_SHEET_NAME = "Products";

  List<ImportProductItem> read(byte[] fileContent, int chunkSize, Consumer<List<ImportProductItem>> chunkConsumer) throws IOException;
}
