package org.atlas.framework.jwt;

public interface JwtService {

  String JWT_PREFIX = "Bearer ";

  String encodeJwt(EncodeJwtInput encodeJwtInput);

  DecodeJwtOutput decodeJwt(DecodeJwtInput input);
}
