package org.atlas.framework.file.excel;

import java.util.List;
import org.atlas.framework.file.model.write.ProductRow;

public interface ProductExcelWriterPort {

   String SHEET_NAME = "Products";

   byte[] write(List<ProductRow> productRows) throws Exception;
}
