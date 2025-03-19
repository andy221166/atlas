package org.atlas.service.product.port.outbound.file.csv;

import java.util.List;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;

public interface ProductCsvReaderPort {

  List<ProductRow> read(byte[] fileContent) throws Exception;
}
