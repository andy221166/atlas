package org.atlas.platform.auth.springsecurity.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.auth.springsecurity.core.TokenService;
import org.atlas.platform.auth.springsecurity.core.UserDetailsImpl;
import org.atlas.service.user.contract.auth.AuthService;
import org.atlas.service.user.contract.command.SignInCommand;
import org.atlas.service.user.contract.command.SignOutCommand;
import org.atlas.service.user.contract.command.SignUpCommand;
import org.atlas.service.user.contract.model.SignInResDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceAdapter implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  @Override
  public void signUp(SignUpCommand command) {
    // Ignored
  }

  @Override
  public SignInResDto signIn(SignInCommand command) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        command.getUsername(), command.getPassword());
    try {
      Authentication authentication = authenticationManager.authenticate(authToken);
      SignInResDto response = toSignInResDto(authentication);
      log.info("Succeeded to sign in user {}", command.getUsername());
      return response;
    } catch (AuthenticationException e) {
      log.error("Failed to sign in user {}", command.getUsername(), e);
      throw e;
    }
  }

  @Override
  public void signOut(SignOutCommand command) {
    tokenService.revokeToken(command.getAuthorizationHeader());
  }

  private SignInResDto toSignInResDto(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String accessToken = tokenService.issueToken(userDetails);
    return new SignInResDto(accessToken);
  }
}
