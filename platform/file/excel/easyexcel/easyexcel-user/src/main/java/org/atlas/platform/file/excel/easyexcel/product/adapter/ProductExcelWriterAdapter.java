package org.atlas.platform.file.excel.easyexcel.product.adapter;

import java.util.List;
import org.atlas.platform.file.excel.easyexcel.core.EasyExcelWriter;
import org.atlas.platform.file.excel.easyexcel.product.model.ProductWriteModel;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.file.excel.ProductExcelWriter;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelWriterAdapter implements ProductExcelWriter {

  @Override
  public byte[] write(List<Product> products, String sheetName) throws Exception {
    List<ProductWriteModel> productWriteModels = ObjectMapperUtil.getInstance()
        .mapList(products, ProductWriteModel.class);
    return EasyExcelWriter.write(productWriteModels, sheetName, ProductWriteModel.class);
  }
}
