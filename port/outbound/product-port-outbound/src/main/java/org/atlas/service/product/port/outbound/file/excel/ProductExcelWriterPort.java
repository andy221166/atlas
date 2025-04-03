package org.atlas.service.product.port.outbound.file.excel;

import java.util.List;
import org.atlas.service.product.port.outbound.file.model.write.ProductRow;

public interface ProductExcelWriterPort {

   String SHEET_NAME = "Products";

   byte[] write(List<ProductRow> productRows) throws Exception;
}
