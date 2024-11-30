package org.atlas.edge.gateway.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.atlas.service.user.domain.shared.enums.Role;

@Data
public class TokenInfo {

  private static class Claims {

    public static final String USER_ID = "user_id";
    public static final String ROLE = "role";
  }

  private final Integer userId;
  private final Role role;

  public TokenInfo(DecodedJWT decodedJWT) {
    this.userId = decodedJWT.getClaim(Claims.USER_ID).asInt();
    this.role = Role.valueOf(decodedJWT.getClaim(Claims.ROLE).asString());
  }
}
