package org.atlas.service.catalog.port.outbound.file.model;

import lombok.Data;
import org.atlas.service.catalog.domain.entity.ProductStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ImportProductItem {

    private Integer id;
    private String name;
    private BigDecimal price;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
    private Integer brandId;
    private String description;
    private List<Integer> categoryIds;
    private String imageUrl;
    private Boolean deleted;
}
