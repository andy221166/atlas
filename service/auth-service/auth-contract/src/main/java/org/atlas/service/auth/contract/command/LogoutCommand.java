package org.atlas.service.auth.contract.command;

import lombok.Data;
import org.atlas.platform.cqrs.model.Command;

@Data
public class LogoutCommand implements Command<Void> {

  private String accessToken;
}
