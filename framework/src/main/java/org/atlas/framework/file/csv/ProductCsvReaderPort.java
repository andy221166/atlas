package org.atlas.framework.file.csv;

import java.util.List;
import org.atlas.framework.file.model.read.ProductRow;

public interface ProductCsvReaderPort {

  List<ProductRow> read(byte[] fileContent) throws Exception;
}
