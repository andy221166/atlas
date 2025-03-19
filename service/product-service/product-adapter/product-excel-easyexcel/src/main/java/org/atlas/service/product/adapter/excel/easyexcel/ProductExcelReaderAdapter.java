package org.atlas.service.product.adapter.excel.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.file.excel.easyexcel.EasyExcelReader;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.outbound.file.excel.ProductExcelReaderPort;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReaderPort {

  @Override
  public List<ProductRow> read(byte[] fileContent) throws IOException {
    List<ProductExcelRow> excelRows =
        EasyExcelReader.read(fileContent, SHEET_NAME, ProductExcelRow.class);
    return ObjectMapperUtil.getInstance().mapList(excelRows, ProductRow.class);
  }

  @Data
  public static class ProductExcelRow {

    @ExcelProperty(value = "Name")
    private String name;

    @ExcelProperty(value = "Price")
    private BigDecimal price;

    @ExcelProperty(value = "Quantity")
    private Integer quantity;

    @ExcelProperty(value = "Status")
    private ProductStatus status;

    @ExcelProperty(value = "Available From")
    @DateTimeFormat(value = Constant.DATE_TIME_FORMAT)
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

    @ExcelProperty(value = "Deleted")
    private Boolean deleted;
  }
}
