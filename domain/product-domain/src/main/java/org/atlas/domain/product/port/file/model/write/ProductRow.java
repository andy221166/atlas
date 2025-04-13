package org.atlas.domain.product.port.file.model.write;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.domain.product.shared.enums.ProductStatus;

@Data
public class ProductRow {

  private Integer id;
  private String name;
  private BigDecimal price;
  private String imageUrl;
  private Integer quantity;
  private ProductStatus status;
  private Date availableFrom;
  private Boolean isActive;
  private String description;
  private String attributeName1;
  private String attributeValue1;
  private String attributeName2;
  private String attributeValue2;
  private String attributeName3;
  private String attributeValue3;
  private Integer brandId;
  private List<Integer> categoryIds;
}
