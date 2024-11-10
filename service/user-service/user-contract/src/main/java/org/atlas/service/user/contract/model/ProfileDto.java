package org.atlas.service.user.contract.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfileDto {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private BigDecimal credit;
}
