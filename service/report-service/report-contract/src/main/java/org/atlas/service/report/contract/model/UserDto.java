package org.atlas.service.report.contract.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private BigDecimal totalAmount;
}