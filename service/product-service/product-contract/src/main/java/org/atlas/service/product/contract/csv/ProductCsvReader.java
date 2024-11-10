package org.atlas.service.product.contract.csv;

import org.atlas.service.product.domain.Product;

import java.io.IOException;
import java.util.List;

public interface ProductCsvReader {

    List<Product> read(byte[] fileContent) throws IOException;
}
