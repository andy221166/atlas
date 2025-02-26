package org.atlas.service.catalog.adapter.csv.opencsv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.platform.file.csv.opencsv.OpenCsvReader;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.domain.entity.ProductStatus;
import org.atlas.service.catalog.port.outbound.file.csv.ProductCsvReader;
import org.atlas.service.catalog.port.outbound.file.model.read.ProductRow;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvReaderAdapter implements ProductCsvReader {

  @Override
  public List<ProductRow> read(byte[] fileContent) throws IOException {
    List<ProductCsvRow> csvRows = OpenCsvReader.read(fileContent, ProductCsvRow.class);
    return ObjectMapperUtil.getInstance().mapList(csvRows, ProductRow.class);
  }

  @Data
  public static class ProductCsvRow {

    @CsvBindByName(column = "ID")
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByName(column = "Name")
    @CsvBindByPosition(position = 1)
    private String name;

    @CsvBindByName(column = "Price")
    @CsvBindByPosition(position = 2)
    private BigDecimal price;

    @CsvBindByName(column = "Status")
    @CsvBindByPosition(position = 3)
    private ProductStatus status;

    @CsvBindByName(column = "Available From")
    @CsvBindByPosition(position = 4)
    private Date availableFrom;

    @CsvBindByName(column = "Active")
    @CsvBindByPosition(position = 5)
    private Boolean isActive;

    @CsvBindByName(column = "Branch ID")
    @CsvBindByPosition(position = 6)
    private Integer brandId;

    @CsvBindByName(column = "Description")
    @CsvBindByPosition(position = 7)
    private String description;

    @CsvBindByName(column = "Image URL")
    @CsvBindByPosition(position = 8)
    private String imageUrl;

    @CsvBindByName(column = "Category IDs")
    @CsvBindByPosition(position = 9)
    private List<Integer> categoryIds;

    @CsvBindByName(column = "Deleted")
    @CsvBindByPosition(position = 10)
    private Boolean deleted;
  }
}
