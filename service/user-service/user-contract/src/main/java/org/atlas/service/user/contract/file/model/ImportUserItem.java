package org.atlas.service.user.contract.file.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.atlas.commons.constant.Patterns;
import org.atlas.commons.enums.Role;

@Data
public class ImportUserItem {

  private Integer id;

  @NotBlank
  private String username;

  @NotBlank
  @Pattern(regexp = Patterns.PASSWORD)
  private String password;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String phoneNumber;

  @NotNull
  private Date birthday;

  private Boolean gender;

  @DecimalMin(value = "0.0")
  private BigDecimal balance = BigDecimal.ZERO;

  @NotNull
  private Role role = Role.USER;

  private String language;

  private String timezone;

  @NotBlank
  private String street;

  @NotBlank
  private String district;

  @NotBlank
  private String city;

  @NotBlank
  private String country;

  @NotBlank
  private String zipCode;

  @NotNull
  private Integer organizationId;

  @NotEmpty
  private List<Integer> groupIds;

  private Boolean deleteFlag;
}
