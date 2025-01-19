package org.atlas.service.auth.contract.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.atlas.platform.cqrs.model.Command;
import org.atlas.service.auth.contract.model.LoginResultDto;

@Data
public class LoginCommand implements Command<LoginResultDto> {

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
