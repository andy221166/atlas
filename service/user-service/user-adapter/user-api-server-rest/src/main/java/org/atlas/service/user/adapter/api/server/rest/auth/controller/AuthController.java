package org.atlas.service.user.adapter.api.server.rest.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.auth.model.LoginRequest;
import org.atlas.service.user.port.inbound.usecase.auth.LoginUseCase;
import org.atlas.service.user.port.inbound.usecase.auth.LogoutUseCase;
import org.springframework.http.HttpHeaders;
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

  @PostMapping("/login")
  public Response<LoginUseCase.Output> login(@Valid @RequestBody LoginRequest request)
      throws Exception {
    LoginUseCase.Input input =
        ObjectMapperUtil.getInstance().map(request, LoginUseCase.Input.class);
    LoginUseCase.Output output = loginUseCase.handle(input);
    return Response.success(output);
  }

  @PostMapping("/logout")
  public Response<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)
      throws Exception {
    LogoutUseCase.Input input = new LogoutUseCase.Input(authorizationHeader);
    logoutUseCase.handle(input);
    return Response.success();
  }
}
