package org.atlas.platform.auth.springsecurityjwt;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.auth.jwt.core.JwtData;
import org.atlas.platform.auth.jwt.core.JwtService;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.service.user.domain.entity.UserEntity;
import org.atlas.service.user.port.outbound.auth.AuthService;
import org.atlas.service.user.port.outbound.auth.LoginRequest;
import org.atlas.service.user.port.outbound.auth.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceAdapter implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public void register(UserEntity userEntity) {
    // Ignored
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    // Perform login
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Generate access token
    Date issuedAt = new Date();
    Date expiresAt = new Date(issuedAt.getTime() + Constant.JWT_EXPIRATION_TIME);
    JwtData jwtData = JwtData.builder()
        .issuer(Constant.JWT_ISSUER)
        .issuedAt(issuedAt)
        .subject(String.valueOf(userDetails.getUserId()))
        .expiredAt(expiresAt)
        .userRole(userDetails.getRole())
        .build();
    final String accessToken = jwtService.issueToken(jwtData);

    // TODO: Generate refresh token

    return new LoginResponse(accessToken, null);
  }

  @Override
  public void logout(String accessToken) {
    jwtService.revokeToken(accessToken, Constant.JWT_ISSUER);
  }
}
