package org.atlas.service.user.contract.command;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.atlas.platform.cqrs.model.Command;

@Data
@AllArgsConstructor
public class DeleteUserCommand implements Command<Void> {

  @NotNull
  private Integer id;
}
