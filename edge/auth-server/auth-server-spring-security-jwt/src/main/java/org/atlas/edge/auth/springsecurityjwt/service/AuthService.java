package org.atlas.edge.auth.springsecurityjwt.service;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import lombok.RequiredArgsConstructor;
import org.atlas.edge.auth.springsecurityjwt.model.LoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.LoginResponse;
import org.atlas.edge.auth.springsecurityjwt.model.LogoutRequest;
import org.atlas.edge.auth.springsecurityjwt.model.RefreshTokenRequest;
import org.atlas.edge.auth.springsecurityjwt.model.RefreshTokenResponse;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.jwt.Jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final TokenService tokenService;

  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest request) throws IOException, InvalidKeySpecException {
    // Perform login
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Issue access token and refresh token
    final String accessToken = tokenService.issueAccessToken(userDetails);
    final String refreshToken = tokenService.issueRefreshToken(userDetails);

    return new LoginResponse(accessToken, refreshToken);
  }

  @Transactional(readOnly = true)
  public RefreshTokenResponse refreshToken(RefreshTokenRequest request)
      throws IOException, InvalidKeySpecException {
    // Validate refresh token
    if (tokenService.isBlacklist(request.getRefreshToken())) {
      throw new BusinessException(AppError.UNAUTHORIZED, "Refresh token was revoked");
    }
    Jwt jwt = tokenService.parseToken(request.getRefreshToken());

    // Find user details via token subject (username)
    UserDetailsImpl userDetails =
        (UserDetailsImpl) userDetailsService.loadUserByUsername(jwt.getSubject());

    // Reissue access token and refresh token
    final String accessToken = tokenService.issueAccessToken(userDetails);
    final String refreshToken = tokenService.issueRefreshToken(userDetails);

    return new RefreshTokenResponse(accessToken, refreshToken);
  }

  public void logout(LogoutRequest request) throws IOException, InvalidKeySpecException {
    tokenService.revokeToken(request.getAccessToken());
    tokenService.revokeToken(request.getRefreshToken());
  }
}
