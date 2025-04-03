package org.atlas.service.user.adapter.api.server.rest.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.auth.model.LoginRequest;
import org.atlas.service.user.adapter.api.server.rest.auth.model.LoginResponse;
import org.atlas.port.inbound.user.auth.LoginUseCase;
import org.atlas.port.inbound.user.auth.LoginUseCase.LoginInput;
import org.atlas.port.inbound.user.auth.LoginUseCase.LoginOutput;
import org.atlas.port.inbound.user.auth.LogoutUseCase;
import org.atlas.port.inbound.user.auth.LogoutUseCase.LogoutInput;
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

  private final LoginUseCase loginUseCase;
  private final LogoutUseCase logoutUseCase;

  @Operation(summary = "User Login", description = "Authenticates a user and returns a login response.")
  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<LoginResponse> login(
      @Parameter(description = "Request object containing user credentials for login.", required = true)
      @Valid @RequestBody LoginRequest request) throws Exception {
    LoginInput input = ObjectMapperUtil.getInstance()
        .map(request, LoginInput.class);
    LoginOutput output = loginUseCase.handle(input);
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
    logoutUseCase.handle(input);
    return Response.success();
  }
}
