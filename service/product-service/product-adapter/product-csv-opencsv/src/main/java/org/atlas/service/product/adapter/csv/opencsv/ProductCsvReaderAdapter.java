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

    @CsvBindByName(column = "Quantity")
    @CsvBindByPosition(position = 2)
    private Integer quantity;

    @CsvBindByName(column = "Status")
    @CsvBindByPosition(position = 3)
    private ProductStatus status;

    @CsvBindByName(column = "Available From")
    @CsvBindByPosition(position = 4)
    private Date availableFrom;

    @CsvBindByName(column = "Branch ID")
    @CsvBindByPosition(position = 5)
    private Integer brandId;

    @CsvBindByName(column = "Description")
    @CsvBindByPosition(position = 6)
    private String description;

    @CsvBindByName(column = "Image URL")
    @CsvBindByPosition(position = 7)
    private String imageUrl;

    @CsvBindByName(column = "Category IDs")
    @CsvBindByPosition(position = 8)
    private List<Integer> categoryIds;
  }
}
