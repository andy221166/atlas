package org.atlas.service.product.port.outbound.file.csv;

import java.util.List;
import org.atlas.service.product.port.outbound.file.model.write.ProductRow;

public interface ProductCsvWriter {

   byte[] write(List<ProductRow> productRows) throws Exception;
}
