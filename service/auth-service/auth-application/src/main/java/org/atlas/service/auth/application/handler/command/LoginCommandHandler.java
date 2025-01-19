package org.atlas.service.auth.application.handler.command;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.auth.contract.command.LoginCommand;
import org.atlas.service.auth.contract.model.LoginResultDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoginCommandHandler implements CommandHandler<LoginCommand, LoginResultDto> {

  private final AuthProvider authProvider;

  @Override
  @Transactional(readOnly = true)
  public LoginResponse handle(LoginCommand command) throws Exception {
    LoginRequest request = ObjectMapperUtil.getInstance().map(command, LoginRequest.class);
    return authProvider.login(request);
  }
}
