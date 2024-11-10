package org.atlas.platform.excel.easyexcel.product;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductReadModel {

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
}
