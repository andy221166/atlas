package org.atlas.service.user.application.handler.command;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.service.user.contract.auth.AuthService;
import org.atlas.service.user.contract.command.SignOutCommand;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignOutCommandHandler implements CommandHandler<SignOutCommand, Void> {

  private final AuthService authService;

  @Override
  public Void handle(SignOutCommand command) throws Exception {
    authService.signOut(command);
    return null;
  }
}
