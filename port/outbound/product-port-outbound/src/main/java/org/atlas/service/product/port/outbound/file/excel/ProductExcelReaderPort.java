package org.atlas.service.product.port.outbound.file.excel;

import java.util.List;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;

public interface ProductExcelReaderPort {

  String SHEET_NAME = "Products";

  List<ProductRow> read(byte[] fileContent) throws Exception;
}
