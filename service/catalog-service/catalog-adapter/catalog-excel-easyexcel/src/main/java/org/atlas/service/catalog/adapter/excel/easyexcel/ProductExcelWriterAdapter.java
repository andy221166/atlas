package org.atlas.service.catalog.adapter.excel.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.platform.file.excel.easyexcel.EasyExcelWriter;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.domain.entity.ProductStatus;
import org.atlas.service.catalog.port.outbound.file.excel.ProductExcelWriter;
import org.atlas.service.catalog.port.outbound.file.model.write.ProductRow;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelWriterAdapter implements ProductExcelWriter {

  @Override
  public byte[] write(List<ProductRow> productRows) throws Exception {
    List<ProductExcelRow> csvRows = ObjectMapperUtil.getInstance().mapList(productRows, ProductExcelRow.class);
    return EasyExcelWriter.write(csvRows, SHEET_NAME, ProductExcelRow.class);
  }

  @Data
  public static class ProductExcelRow {

    @ExcelProperty(value = "ID")
    private Integer id;

    @ExcelProperty(value = "Code")
    private String code;

    @ExcelProperty(value = "Name")
    private String name;

    @ExcelProperty(value = "Price")
    private BigDecimal price;

    @ExcelProperty(value = "Status", converter = ProductStatusConverter.class)
    private ProductStatus status;

    @ExcelProperty(value = "Available From")
    private Date availableFrom;

    @ExcelProperty(value = "Active")
    private Boolean isActive;

    @ExcelProperty(value = "Branch ID")
    private Integer brandId;

    @ExcelProperty(value = "Description")
    private String description;

    @ExcelProperty(value = "Image URL")
    private String imageUrl;

    @ExcelProperty(value = "Category IDs")
    private List<Integer> categoryIds;
  }
}
