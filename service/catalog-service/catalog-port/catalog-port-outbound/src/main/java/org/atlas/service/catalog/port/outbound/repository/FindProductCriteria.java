package org.atlas.service.catalog.port.outbound.repository;

import lombok.Data;
import org.atlas.service.catalog.domain.entity.ProductStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class FindProductCriteria {

    private Integer id;
    private String keyword; // Find by name or description
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private ProductStatus status;
    private Date availableFrom;
    private Boolean isActive;
    private Integer brandId;
    private List<Integer> categoryIds;
}
