package org.atlas.framework.file.csv;

import java.util.List;
import org.atlas.framework.file.model.write.ProductRow;

public interface ProductCsvWriterPort {

   byte[] write(List<ProductRow> productRows) throws Exception;
}
