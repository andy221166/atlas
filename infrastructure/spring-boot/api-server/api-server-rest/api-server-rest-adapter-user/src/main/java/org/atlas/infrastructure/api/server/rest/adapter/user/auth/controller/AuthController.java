package org.atlas.infrastructure.api.server.rest.adapter.user.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.auth.AuthLoginUseCaseHandler;
import org.atlas.domain.user.usecase.auth.AuthLoginUseCaseHandler.LoginInput;
import org.atlas.domain.user.usecase.auth.AuthLoginUseCaseHandler.LoginOutput;
import org.atlas.domain.user.usecase.auth.AuthLogoutUseCaseHandler;
import org.atlas.domain.user.usecase.auth.AuthLogoutUseCaseHandler.LogoutInput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.user.auth.model.LoginRequest;
import org.atlas.infrastructure.api.server.rest.adapter.user.auth.model.LoginResponse;
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

  private final AuthLoginUseCaseHandler authLoginUseCaseHandler;
  private final AuthLogoutUseCaseHandler authLogoutUseCaseHandler;

  @Operation(summary = "User Login", description = "Authenticates a user and returns a login response.")
  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<LoginResponse> login(
      @Parameter(description = "Request object containing user credentials for login.", required = true)
      @Valid @RequestBody LoginRequest request) throws Exception {
    LoginInput input = ObjectMapperUtil.getInstance()
        .map(request, LoginInput.class);
    LoginOutput output = authLoginUseCaseHandler.handle(input);
    LoginResponse response = ObjectMapperUtil.getInstance()
        .map(output, LoginResponse.class);
    return Response.success(response);
  }

  @Operation(summary = "User Logout", description = "Logs out a user using the provided authorization header.")
  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> logout(
      @Parameter(description = "Authorization header containing the user's token.", required = true, example = "Bearer <token>")
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
    LogoutInput input = new LogoutInput(authorizationHeader);
    authLogoutUseCaseHandler.handle(input);
    return Response.success();
  }
}
