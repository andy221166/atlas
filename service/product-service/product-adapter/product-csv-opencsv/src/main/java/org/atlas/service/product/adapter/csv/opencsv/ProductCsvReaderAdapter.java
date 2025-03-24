package org.atlas.service.product.adapter.csv.opencsv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.platform.file.csv.opencsv.OpenCsvReader;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.outbound.file.csv.ProductCsvReaderPort;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvReaderAdapter implements ProductCsvReaderPort {

  @Override
  public List<ProductRow> read(byte[] fileContent) throws IOException {
    List<ProductCsvRow> csvRows = OpenCsvReader.read(fileContent, ProductCsvRow.class);
    return ObjectMapperUtil.getInstance().mapList(csvRows, ProductRow.class);
  }

  @Data
  public static class ProductCsvRow {

    @CsvBindByName(column = "Name")
    @CsvBindByPosition(position = 0)
    private String name;

    @CsvBindByName(column = "Price")
    @CsvBindByPosition(position = 1)
    private BigDecimal price;

    @CsvBindByName(column = "Image URL")
    @CsvBindByPosition(position = 2)
    private String imageUrl;

    @CsvBindByName(column = "Quantity")
    @CsvBindByPosition(position = 3)
    private Integer quantity;

    @CsvBindByName(column = "Status")
    @CsvBindByPosition(position = 4)
    private ProductStatus status;

    @CsvBindByName(column = "Available From")
    @CsvBindByPosition(position = 5)
    private Date availableFrom;

    @CsvBindByName(column = "Active")
    @CsvBindByPosition(position = 6)
    private Boolean isActive;

    @CsvBindByName(column = "Description")
    @CsvBindByPosition(position = 7)
    private String description;

    @CsvBindByName(column = "Attribute Name 1")
    @CsvBindByPosition(position = 8)
    private String attributeName1;

    @CsvBindByName(column = "Attribute Value 1")
    @CsvBindByPosition(position = 9)
    private String attributeValue1;

    @CsvBindByName(column = "Attribute Name 1")
    @CsvBindByPosition(position = 10)
    private String attributeName2;

    @CsvBindByName(column = "Attribute Value 2")
    @CsvBindByPosition(position = 11)
    private String attributeValue2;

    @CsvBindByName(column = "Attribute Name 3")
    @CsvBindByPosition(position = 12)
    private String attributeName3;

    @CsvBindByName(column = "Attribute Value 3")
    @CsvBindByPosition(position = 13)
    private String attributeValue3;

    @CsvBindByName(column = "Branch ID")
    @CsvBindByPosition(position = 14)
    private Integer brandId;

    @CsvBindByName(column = "Category IDs")
    @CsvBindByPosition(position = 15)
    private List<Integer> categoryIds;
  }
}
