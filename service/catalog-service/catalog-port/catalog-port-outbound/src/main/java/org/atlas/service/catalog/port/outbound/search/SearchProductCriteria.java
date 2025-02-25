package org.atlas.service.catalog.port.outbound.search;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SearchProductCriteria {

    private String keyword; // Find by name or description
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer brandId;
    private List<Integer> categoryIds;
}
