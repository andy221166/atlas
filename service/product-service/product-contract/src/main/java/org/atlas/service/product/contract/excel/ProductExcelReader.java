package org.atlas.service.product.contract.excel;

import org.atlas.service.product.domain.Product;

import java.io.IOException;
import java.util.List;

public interface ProductExcelReader {

    List<Product> read(byte[] fileContent) throws IOException;
}
