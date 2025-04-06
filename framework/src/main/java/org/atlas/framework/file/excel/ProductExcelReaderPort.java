package org.atlas.framework.file.excel;

import java.util.List;
import org.atlas.framework.file.model.read.ProductRow;

public interface ProductExcelReaderPort {

  String SHEET_NAME = "Products";

  List<ProductRow> read(byte[] fileContent) throws Exception;
}
