package org.atlas.service.user.contract.auth;

import org.atlas.service.user.contract.command.SignInCommand;
import org.atlas.service.user.contract.command.SignOutCommand;
import org.atlas.service.user.contract.command.SignUpCommand;
import org.atlas.service.user.contract.model.SignInResDto;

public interface AuthService {

  void signUp(SignUpCommand command);

  SignInResDto signIn(SignInCommand command);

  void signOut(SignOutCommand command);
}
