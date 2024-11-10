package org.atlas.service.product.contract.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private CategoryDto category;
}
