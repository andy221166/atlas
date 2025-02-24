package org.atlas.service.product.contract.excel;

import java.io.IOException;
import java.util.List;
import org.atlas.service.product.domain.entity.Product;

public interface ProductExcelReader {

  List<Product> read(byte[] fileContent) throws IOException;
}
