package org.atlas.service.user.contract.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.atlas.platform.cqrs.model.Command;
import org.atlas.service.user.contract.model.SignInResDto;

@Data
public class SignInCommand implements Command<SignInResDto> {

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
