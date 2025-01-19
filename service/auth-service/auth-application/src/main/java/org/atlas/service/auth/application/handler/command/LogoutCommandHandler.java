package org.atlas.service.auth.application.handler.command;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.auth.contract.command.LogoutCommand;
import org.atlas.service.auth.contract.repository.AuthProvider;
import org.atlas.service.auth.contract.repository.LogoutRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutCommandHandler implements CommandHandler<LogoutCommand, Void> {

  private final AuthProvider authProvider;

  @Override
  public Void handle(LogoutCommand command) throws Exception {
    LogoutRequest request = ObjectMapperUtil.getInstance().map(command, LogoutRequest.class);
    authProvider.logout(request);
    return null;
  }
}
