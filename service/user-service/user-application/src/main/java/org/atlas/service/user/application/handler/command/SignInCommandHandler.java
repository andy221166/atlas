package org.atlas.service.user.application.handler.command;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.user.contract.auth.AuthService;
import org.atlas.service.user.contract.command.SignInCommand;
import org.atlas.service.user.contract.model.SignInResDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SignInCommandHandler implements CommandHandler<SignInCommand, SignInResDto> {

  private final AuthService authService;

  @Override
  @Transactional(readOnly = true)
  public SignInResDto handle(SignInCommand command) throws Exception {
    return authService.signIn(command);
  }
}
