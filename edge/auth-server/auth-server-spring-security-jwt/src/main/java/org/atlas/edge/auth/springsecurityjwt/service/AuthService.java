package org.atlas.edge.auth.springsecurityjwt.service;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.auth.entity.UserEntity;
import org.atlas.domain.auth.entity.SessionEntity;
import org.atlas.domain.auth.repository.UserRepository;
import org.atlas.domain.auth.repository.SessionRepository;
import org.atlas.edge.auth.springsecurityjwt.exception.InvalidTokenException;
import org.atlas.edge.auth.springsecurityjwt.mapper.AuthMapper;
import org.atlas.edge.auth.springsecurityjwt.model.GenerateOneTimeTokenRequest;
import org.atlas.edge.auth.springsecurityjwt.model.GenerateOneTimeTokenResponse;
import org.atlas.edge.auth.springsecurityjwt.model.LoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.LoginResponse;
import org.atlas.edge.auth.springsecurityjwt.model.OneTimeTokenLoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.RefreshTokenRequest;
import org.atlas.edge.auth.springsecurityjwt.model.RefreshTokenResponse;
import org.atlas.edge.auth.springsecurityjwt.security.UserDetailsImpl;
import org.atlas.framework.constant.SecurityConstant;
import org.atlas.framework.context.UserContext;
import org.atlas.framework.context.UserInfo;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;
import org.atlas.framework.jwt.Jwt;
import org.atlas.framework.util.UUIDGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final OneTimeTokenService oneTimeTokenService;
  private final RedisTemplate<String, Object> redisTemplate;

  @Transactional
  public LoginResponse login(LoginRequest request) throws IOException, InvalidKeySpecException {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword());
    return doLogin(authentication, request.getDeviceId());
  }

  @Transactional
  public LoginResponse oneTimeTokenLogin(OneTimeTokenLoginRequest request)
      throws IOException, InvalidKeySpecException {
    OneTimeTokenAuthenticationToken authentication =
        new OneTimeTokenAuthenticationToken(request.getIdentifier(), request.getToken());
    return doLogin(authentication, request.getDeviceId());
  }

  public GenerateOneTimeTokenResponse generateOneTimeToken(GenerateOneTimeTokenRequest request) {
    OneTimeToken token = oneTimeTokenService.generate(
        new org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest(
            request.getIdentifier()));
    return new GenerateOneTimeTokenResponse(token.getTokenValue());
  }

  @Transactional
  public RefreshTokenResponse refreshToken(RefreshTokenRequest request)
      throws IOException, InvalidKeySpecException {
    // Retrieve session from database
    String sessionId = UserContext.getSessionId();
    SessionEntity sessionEntity = sessionRepository.findById(sessionId)
        .orElseThrow(() -> new BusinessException(AppError.UNAUTHORIZED, "Session not found"));

    // Validate refresh token
    if (!sessionEntity.getRefreshToken().equals(request.getRefreshToken())) {
      throw new BusinessException(AppError.UNAUTHORIZED, "Invalid refresh token");
    }

    try {
      tokenService.parseToken(request.getRefreshToken());
    } catch (InvalidTokenException e) {
      throw new BusinessException(AppError.UNAUTHORIZED, "Invalid refresh token");
    }

    // Validate device ID
    if (!sessionEntity.getDeviceId().equals(request.getDeviceId())) {
      throw new BusinessException(AppError.UNAUTHORIZED, "Invalid device ID");
    }

    // Reissue tokens
    UserEntity userEntity = userRepository.findById(sessionEntity.getUserId())
        .orElseThrow(() -> new BusinessException(AppError.USER_NOT_FOUND));
    UserDetailsImpl userDetails = AuthMapper.map(userEntity);

    // Issue new access token
    Date now = new Date();
    Date accessTokenExpiresAt = new Date(
        now.getTime() + SecurityConstant.ACCESS_TOKEN_EXPIRATION_TIME * 1000);
    String accessToken = tokenService.issueAccessToken(userDetails, now, accessTokenExpiresAt);

    // Issue new refresh token
    now = new Date();
    Date refreshTokenExpiresAt = new Date(
        now.getTime() + SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME * 1000);
    String refreshToken = tokenService.issueRefreshToken(userDetails, now, refreshTokenExpiresAt);

    // Update refresh token in database
    sessionEntity.setRefreshToken(refreshToken);
    sessionRepository.update(sessionEntity);

    return new RefreshTokenResponse(accessToken, refreshToken);
  }

  @Transactional
  public void logout(String accessToken) {
    // Need to parse the access token to get expiredAt
    Jwt jwt;
    try {
      jwt = tokenService.parseToken(accessToken);
    } catch (InvalidTokenException e) {
      throw new BusinessException(AppError.UNAUTHORIZED, "Invalid access token");
    }

    UserInfo currentUser = UserContext.get();

    // Remove session from database
    sessionRepository.deleteById(currentUser.getSessionId());

    // Revoke session
    revokeSession(currentUser.getSessionId(), jwt.getExpiresAt());

    // Update last logout timestamp in Redis
    updateLastLogoutTs(currentUser.getUserId());
  }

  public void forceLogoutOnAllDevices() {
    Integer userId = UserContext.getUserId();
    updateLastLogoutTs(userId);
  }

  private LoginResponse doLogin(Authentication authenticationRequest, String deviceId)
      throws IOException, InvalidKeySpecException {
    Authentication authentication = authenticationManager.authenticate(authenticationRequest);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Issue refresh token
    Date now = new Date();
    Date refreshTokenExpiresAt = new Date(
        now.getTime() + SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME * 1000);
    String refreshToken = tokenService.issueRefreshToken(userDetails, now, refreshTokenExpiresAt);

    // Create new session
    SessionEntity sessionEntity = new SessionEntity();
    sessionEntity.setId(UUIDGenerator.generate());
    sessionEntity.setUserId(userDetails.getUserId());
    sessionEntity.setDeviceId(deviceId);
    sessionEntity.setRefreshToken(refreshToken);
    sessionRepository.insert(sessionEntity);

    userDetails.setSessionId(sessionEntity.getId());

    // Issue access token
    now = new Date();
    Date accessTokenExpiresAt = new Date(
        now.getTime() + SecurityConstant.ACCESS_TOKEN_EXPIRATION_TIME * 1000);
    String accessToken = tokenService.issueAccessToken(userDetails, now, accessTokenExpiresAt);

    return new LoginResponse(accessToken, refreshToken);
  }

  private void revokeSession(String sessionId, Date expiresAt) {
    String redisKey = String.format(SecurityConstant.SESSION_BLACKLISTED_REDIS_KEY_FORMAT, sessionId);
    long remainingMillis = expiresAt.getTime() - System.currentTimeMillis();
    if (remainingMillis > 0) {
      redisTemplate.opsForValue().set(redisKey, true, Duration.ofMillis(remainingMillis));
    }
  }

  private void updateLastLogoutTs(Integer userId) {
    long lastLogoutTs = new Date().getTime();
    String redisKey = String.format("user:%d:lastLogoutTs", userId);
    redisTemplate.opsForValue().set(redisKey, lastLogoutTs);
  }
}
