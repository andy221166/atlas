package org.atlas.platform.file.csv.opencsv.product.adapter;

import java.util.List;
import org.atlas.platform.file.csv.opencsv.core.OpenCsvWriter;
import org.atlas.platform.file.csv.opencsv.product.model.ProductWriteModel;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.file.csv.ProductCsvWriter;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvWriterAdapter implements ProductCsvWriter {

  @Override
  public byte[] write(List<Product> products) throws Exception {
    List<ProductWriteModel> productWriteModels = ObjectMapperUtil.getInstance()
        .mapList(products, ProductWriteModel.class);
    return OpenCsvWriter.write(productWriteModels, ProductWriteModel.class);
  }
}
