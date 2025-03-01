package org.atlas.service.product.adapter.csv.opencsv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.platform.file.csv.opencsv.OpenCsvWriter;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.outbound.file.csv.ProductCsvWriter;
import org.atlas.service.product.port.outbound.file.model.write.ProductRow;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvWriterAdapter implements ProductCsvWriter {

  @Override
  public byte[] write(List<ProductRow> productRows) throws Exception {
    List<ProductCsvRow> csvRows = ObjectMapperUtil.getInstance()
        .mapList(productRows, ProductCsvRow.class);
    return OpenCsvWriter.write(csvRows, ProductCsvRow.class);
  }

  @Data
  public static class ProductCsvRow {

    @CsvBindByName(column = "ID")
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByName(column = "Code")
    @CsvBindByPosition(position = 1)
    private String code;

    @CsvBindByName(column = "Name")
    @CsvBindByPosition(position = 2)
    private String name;

    @CsvBindByName(column = "Price")
    @CsvBindByPosition(position = 3)
    private BigDecimal price;

    @CsvBindByName(column = "Quantity")
    @CsvBindByPosition(position = 4)
    private Integer quantity;

    @CsvBindByName(column = "Status")
    @CsvBindByPosition(position = 5)
    private ProductStatus status;

    @CsvBindByName(column = "Available From")
    @CsvBindByPosition(position = 6)
    private Date availableFrom;

    @CsvBindByName(column = "Active")
    @CsvBindByPosition(position = 7)
    private Boolean isActive;

    @CsvBindByName(column = "Branch ID")
    @CsvBindByPosition(position = 8)
    private Integer brandId;

    @CsvBindByName(column = "Description")
    @CsvBindByPosition(position = 9)
    private String description;

    @CsvBindByName(column = "Image URL")
    @CsvBindByPosition(position = 10)
    private String imageUrl;

    @CsvBindByName(column = "Category IDs")
    @CsvBindByPosition(position = 11)
    private List<Integer> categoryIds;
  }
}
