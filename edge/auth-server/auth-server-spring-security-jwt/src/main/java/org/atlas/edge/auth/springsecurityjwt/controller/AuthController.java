package org.atlas.edge.auth.springsecurityjwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.edge.auth.springsecurityjwt.model.LoginRequest;
import org.atlas.edge.auth.springsecurityjwt.model.LoginResponse;
import org.atlas.edge.auth.springsecurityjwt.model.CreateUserRequest;
import org.atlas.edge.auth.springsecurityjwt.service.AuthService;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Auth user registration", description = "Register user into the auth system.")
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> register(
      @Parameter(description = "Request object containing registration data.", required = true)
      @Valid @RequestBody CreateUserRequest request) throws Exception {
    authService.register(request);
    return Response.success();
  }

  @Operation(summary = "User Login", description = "Authenticates a user and returns a login response.")
  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<LoginResponse> login(
      @Parameter(description = "Request object containing user credentials for login.", required = true)
      @Valid @RequestBody LoginRequest request) throws Exception {
    LoginResponse response = authService.login(request);
    return Response.success(response);
  }

  @Operation(summary = "User Logout", description = "Logs out a user using the provided authorization header.")
  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> logout(
      @Parameter(description = "Authorization header containing the user's token.", required = true, example = "Bearer <token>")
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
    authService.logout(authorizationHeader);
    return Response.success();
  }
}
