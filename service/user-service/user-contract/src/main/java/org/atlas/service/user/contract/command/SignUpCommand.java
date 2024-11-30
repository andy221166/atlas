package org.atlas.service.user.contract.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.atlas.commons.constant.Patterns;
import org.atlas.platform.cqrs.model.Command;

@Data
public class SignUpCommand implements Command<Void> {

  @NotBlank
  private String username;

  @NotBlank
  @Pattern(regexp = Patterns.PASSWORD, message = "{error.user.invalid_password}")
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
