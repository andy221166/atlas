package org.atlas.service.user.adapter.api.server.rest.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.auth.model.LoginRequest;
import org.atlas.service.user.adapter.api.server.rest.auth.model.LoginResponse;
import org.atlas.service.user.port.inbound.auth.LoginUseCase.LoginInput;
import org.atlas.service.user.port.inbound.auth.LoginUseCase.LoginOutput;
import org.atlas.service.user.port.inbound.auth.LogoutUseCase.LogoutInput;
import org.atlas.service.user.port.inbound.auth.LoginUseCase;
import org.atlas.service.user.port.inbound.auth.LogoutUseCase;
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

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request)
      throws Exception {
    LoginInput input = ObjectMapperUtil.getInstance().map(request, LoginInput.class);
    LoginOutput output = loginUseCase.handle(input);
    LoginResponse response = ObjectMapperUtil.getInstance().map(output, LoginResponse.class);
    return Response.success(response);
  }

  @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)
      throws Exception {
    LogoutInput input = new LogoutInput(authorizationHeader);
    logoutUseCase.handle(input);
    return Response.success();
  }
}
