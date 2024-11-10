package org.atlas.service.report.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Product implements Serializable {

    private Integer id;
    private String name;
    private BigDecimal price;
}
