package org.atlas.platform.auth.springsecurityjwt;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.jwt.core.JwtData;
import org.atlas.platform.jwt.core.JwtService;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.domain.user.entity.UserEntity;
import org.atlas.service.user.port.outbound.auth.AuthPort;
import org.atlas.service.user.port.outbound.auth.AuthLoginRequest;
import org.atlas.service.user.port.outbound.auth.AuthLoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthAdapter implements AuthPort {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public void register(UserEntity userEntity) {
    // Ignored
  }

  @Override
  public AuthLoginResponse login(AuthLoginRequest request) {
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
        .audience(Constant.JWT_AUDIENCE)
        .expiredAt(expiresAt)
        .userId(userDetails.getUserId())
        .userRole(userDetails.getRole())
        .build();
    final String accessToken = jwtService.issueToken(jwtData);

    // TODO: Generate refresh token

    return new AuthLoginResponse(accessToken, null);
  }

  @Override
  public void logout(String accessToken) {
    jwtService.revokeToken(accessToken, Constant.JWT_ISSUER);
  }
}
