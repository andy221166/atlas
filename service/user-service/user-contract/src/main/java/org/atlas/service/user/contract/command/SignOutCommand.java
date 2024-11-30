package org.atlas.service.user.contract.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.cqrs.model.Command;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignOutCommand implements Command<Void> {

  @NotBlank
  private String authorizationHeader;
}
