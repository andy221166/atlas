package org.atlas.platform.excel.easyexcel.product;

import java.io.IOException;
import java.util.List;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.excel.easyexcel.core.EasyExcelReader;
import org.atlas.service.product.contract.excel.ProductExcelReader;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReader {

  @Override
  public List<Product> read(byte[] fileContent) throws IOException {
    List<ProductReadModel> excelProducts = EasyExcelReader.read(fileContent,
        ProductReadModel.class);
    return excelProducts.stream()
        .map(excelProduct -> {
          Product product = ModelMapperUtil.map(excelProduct, Product.class);
          product.setCategory(new Category(excelProduct.getCategoryId()));
          return product;
        })
        .toList();
  }
}
