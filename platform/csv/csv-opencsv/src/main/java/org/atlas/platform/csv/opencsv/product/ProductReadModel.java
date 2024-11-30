package org.atlas.platform.csv.opencsv.product;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductReadModel {

  @CsvBindByName(column = "Name")
  @CsvBindByPosition(position = 0)
  private String name;

  @CsvBindByName(column = "Description")
  @CsvBindByPosition(position = 1)
  private String description;

  @CsvBindByName(column = "Price")
  @CsvBindByPosition(position = 2)
  private BigDecimal price;

  @CsvBindByName(column = "Quantity")
  @CsvBindByPosition(position = 3)
  private Integer quantity;

  @CsvBindByName(column = "Category ID")
  @CsvBindByPosition(position = 4)
  private Integer categoryId;
}
