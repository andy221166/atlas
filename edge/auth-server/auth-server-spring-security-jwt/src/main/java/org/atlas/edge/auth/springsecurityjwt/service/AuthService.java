package org.atlas.edge.auth.springsecurityjwt.service;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import lombok.RequiredArgsConstructor;
import org.atlas.edge.auth.springsecurityjwt.model.LoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.LoginResponse;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest request) throws IOException, InvalidKeySpecException {
    // Perform login
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Generate token
    final String accessToken = tokenService.issueAccessToken(userDetails);
    final String refreshToken = tokenService.issueRefreshToken(userDetails);

    return new LoginResponse(accessToken, refreshToken);
  }

  public void logout(String accessToken) throws IOException, InvalidKeySpecException {
    tokenService.revokeAccessToken(accessToken);
  }
}
