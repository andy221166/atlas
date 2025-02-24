package org.atlas.service.product.contract.csv;

import java.io.IOException;
import java.util.List;
import org.atlas.service.product.domain.entity.Product;

public interface ProductCsvReader {

  List<Product> read(byte[] fileContent) throws IOException;
}
