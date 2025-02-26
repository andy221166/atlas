package org.atlas.service.catalog.port.outbound.file.excel;

import java.util.List;
import org.atlas.service.catalog.port.outbound.file.model.write.ProductRow;

public interface ProductExcelWriter {

   String SHEET_NAME = "Products";

   byte[] write(List<ProductRow> productRows) throws Exception;
}
