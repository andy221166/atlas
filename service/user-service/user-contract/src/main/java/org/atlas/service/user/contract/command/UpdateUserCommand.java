package org.atlas.service.user.contract.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserCommand extends CreateUserCommand {

  @NotNull
  private Integer id;

}
