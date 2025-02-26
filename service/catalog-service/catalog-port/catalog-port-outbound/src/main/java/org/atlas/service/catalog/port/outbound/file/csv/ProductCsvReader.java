package org.atlas.service.catalog.port.outbound.file.csv;

import java.util.List;
import org.atlas.service.catalog.port.outbound.file.model.read.ProductRow;

public interface ProductCsvReader {

  List<ProductRow> read(byte[] fileContent) throws Exception;
}
