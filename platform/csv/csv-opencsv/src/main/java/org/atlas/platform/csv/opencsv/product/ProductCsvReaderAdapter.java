package org.atlas.platform.csv.opencsv.product;

import java.io.IOException;
import java.util.List;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.csv.opencsv.core.OpenCsvReader;
import org.atlas.service.product.contract.csv.ProductCsvReader;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvReaderAdapter implements ProductCsvReader {

  @Override
  public List<Product> read(byte[] fileContent) throws IOException {
    List<ProductReadModel> csvProducts = OpenCsvReader.read(fileContent, ProductReadModel.class);
    return csvProducts.stream()
        .map(csvProduct -> {
          Product product = ModelMapperUtil.map(csvProduct, Product.class);
          product.setCategory(new Category(csvProduct.getCategoryId()));
          return product;
        })
        .toList();
  }
}
