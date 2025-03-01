package org.atlas.service.product.port.outbound.file.excel;

import java.util.List;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;

public interface ProductExcelReader {

  String SHEET_NAME = "Products";

  List<ProductRow> read(byte[] fileContent) throws Exception;
}
