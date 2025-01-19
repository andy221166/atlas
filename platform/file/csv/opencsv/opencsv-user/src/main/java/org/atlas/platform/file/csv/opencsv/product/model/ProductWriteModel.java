package org.atlas.platform.file.csv.opencsv.product.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductWriteModel {

  @CsvBindByName(column = "ID")
  @CsvBindByPosition(position = 0)
  private Integer id;

  @CsvBindByName(column = "Name")
  @CsvBindByPosition(position = 1)
  private String name;

  @CsvBindByName(column = "Description")
  @CsvBindByPosition(position = 2)
  private String description;

  @CsvBindByName(column = "Price")
  @CsvBindByPosition(position = 3)
  private BigDecimal price;

  @CsvBindByName(column = "Quantity")
  @CsvBindByPosition(position = 4)
  private Integer quantity;

  @CsvBindByName(column = "Category ID")
  @CsvBindByPosition(position = 5)
  private Integer categoryId;
}
