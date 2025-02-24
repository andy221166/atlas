package org.atlas.service.user.adapter.api.server.rest.storefront.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.atlas.platform.commons.constant.Patterns;

@Data
public class RegisterRequest {

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
}
