package org.atlas.infrastructure.file.excel.easyexcel.adapter.product;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.domain.product.port.file.excel.ProductExcelReaderPort;
import org.atlas.domain.product.port.file.model.read.ProductRow;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.file.excel.easyexcel.core.EasyExcelReader;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelReaderAdapter implements ProductExcelReaderPort {

  @Override
  public List<ProductRow> read(byte[] fileContent) throws IOException {
    List<ProductExcelRow> excelRows =
        EasyExcelReader.read(fileContent, SHEET_NAME, ProductExcelRow.class);
    return ObjectMapperUtil.getInstance()
        .mapList(excelRows, ProductRow.class);
  }

  @Data
  public static class ProductExcelRow {

    @ExcelProperty(value = "Name")
    private String name;

    @ExcelProperty(value = "Price")
    private BigDecimal price;

    @ExcelProperty(value = "Image URL")
    private String imageUrl;

    @ExcelProperty(value = "Quantity")
    private Integer quantity;

    @ExcelProperty(value = "Status")
    private ProductStatus status;

    @ExcelProperty(value = "Available From")
    @DateTimeFormat(value = CommonConstant.DATE_TIME_FORMAT)
    private Date availableFrom;

    @ExcelProperty(value = "Active")
    private Boolean isActive;

    @ExcelProperty(value = "Description")
    private String description;

    @ExcelProperty(value = "Attribute Name 1")
    private String attributeName1;

    @ExcelProperty(value = "Attribute Value 1")
    private String attributeValue1;

    @ExcelProperty(value = "Attribute Name 2")
    private String attributeName2;

    @ExcelProperty(value = "Attribute Value 2")
    private String attributeValue2;

    @ExcelProperty(value = "Attribute Name 3")
    private String attributeName3;

    @ExcelProperty(value = "Attribute Value 3")
    private String attributeValue3;

    @ExcelProperty(value = "Branch ID")
    private Integer brandId;

    @ExcelProperty(value = "Category IDs")
    private List<Integer> categoryIds;
  }
}
