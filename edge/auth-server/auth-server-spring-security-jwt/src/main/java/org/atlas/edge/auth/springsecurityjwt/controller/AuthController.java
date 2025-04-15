package org.atlas.edge.auth.springsecurityjwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.edge.auth.springsecurityjwt.model.GenerateOneTimeTokenRequest;
import org.atlas.edge.auth.springsecurityjwt.model.GenerateOneTimeTokenResponse;
import org.atlas.edge.auth.springsecurityjwt.model.LoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.LoginResponse;
import org.atlas.edge.auth.springsecurityjwt.model.LogoutRequest;
import org.atlas.edge.auth.springsecurityjwt.model.OneTimeTokenLoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.RefreshTokenRequest;
import org.atlas.edge.auth.springsecurityjwt.model.RefreshTokenResponse;
import org.atlas.edge.auth.springsecurityjwt.service.AuthService;
import org.atlas.edge.auth.springsecurityjwt.service.CookieService;
import org.atlas.framework.api.server.rest.response.Response;
import org.atlas.framework.constant.SecurityConstant;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final CookieService cookieService;

  @Operation(summary = "User Login", description = "Authenticates a user and returns a login response.")
  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<LoginResponse> login(
      @Parameter(description = "Request object containing user credentials for login.", required = true)
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse httpServletResponse) throws Exception {
    LoginResponse loginResponse = authService.login(request);
    addTokensToCookies(httpServletResponse, loginResponse);
    return Response.success(loginResponse);
  }

  @PostMapping("/ott/login")
  public Response<LoginResponse> oneTimeTokenLogin(
      @Valid @RequestBody OneTimeTokenLoginRequest request,
      HttpServletResponse httpServletResponse) throws Exception {
    LoginResponse loginResponse = authService.oneTimeTokenLogin(request);
    addTokensToCookies(httpServletResponse, loginResponse);
    return Response.success(loginResponse);
  }

  @PostMapping("/ott/generate")
  public Response<GenerateOneTimeTokenResponse> generateOneTimeToken(
      @Valid @RequestBody GenerateOneTimeTokenRequest request) {
    GenerateOneTimeTokenResponse response = authService.generateOneTimeToke(request);
    return Response.success(response);
  }

  @Operation(summary = "Refresh Token", description = "Issues a new access token using a valid refresh token.")
  @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<RefreshTokenResponse> refreshToken(
      @Parameter(description = "Refresh token sent in the request body", required = true)
      @Valid @RequestBody RefreshTokenRequest request) throws Exception {
    RefreshTokenResponse response = authService.refreshToken(request);
    return Response.success(response);
  }

  @Operation(summary = "User Logout", description = "Logs out a user using the provided authorization header.")
  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> logout(
      @Parameter(description = "Logout request includes access token and refresh token", required = true)
      @Valid @RequestBody LogoutRequest request,
      HttpServletResponse httpServletResponse) throws Exception {
    authService.logout(request);
    cookieService.deleteCookie(httpServletResponse, SecurityConstant.ACCESS_TOKEN_COOKIE);
    cookieService.deleteCookie(httpServletResponse, SecurityConstant.REFRESH_TOKEN_COOKIE);
    return Response.success();
  }

  private void addTokensToCookies(HttpServletResponse httpServletResponse,
      LoginResponse loginResponse) {
    cookieService.addCookie(httpServletResponse,
        SecurityConstant.ACCESS_TOKEN_COOKIE,
        loginResponse.getAccessToken(),
        SecurityConstant.ACCESS_TOKEN_EXPIRATION_TIME);
    cookieService.addCookie(httpServletResponse,
        SecurityConstant.REFRESH_TOKEN_COOKIE,
        loginResponse.getRefreshToken(),
        SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME);
  }
}
