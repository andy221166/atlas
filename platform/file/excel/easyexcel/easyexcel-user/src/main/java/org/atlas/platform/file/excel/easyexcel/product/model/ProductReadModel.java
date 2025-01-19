package org.atlas.platform.file.excel.easyexcel.product.model;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductReadModel {

  @ExcelProperty(value = "ID")
  private Integer id;

  @ExcelProperty(value = "Name")
  private String name;

  @ExcelProperty(value = "Description")
  private String description;

  @ExcelProperty(value = "Price")
  private BigDecimal price;

  @ExcelProperty(value = "Quantity")
  private Integer quantity;

  @ExcelProperty(value = "Category ID")
  private Integer categoryId;

  @ExcelProperty(value = "Delete flag")
  private Boolean deleteFlag;
}
