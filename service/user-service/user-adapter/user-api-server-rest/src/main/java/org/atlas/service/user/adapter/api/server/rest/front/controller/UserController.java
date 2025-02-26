package org.atlas.service.user.adapter.api.server.rest.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.front.model.RegisterRequest;
import org.atlas.service.user.port.inbound.front.GetProfileUseCase;
import org.atlas.service.user.port.inbound.front.RegisterUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontUserController")
@RequestMapping("api/storefront/users")
@Validated
@RequiredArgsConstructor
public class UserController {

  private final RegisterUseCase registerUseCase;
  private final GetProfileUseCase getProfileUseCase;

  @PostMapping("/register")
  public Response<Void> register(@Valid @RequestBody RegisterRequest request) throws Exception {
    RegisterUseCase.Input input =
        ObjectMapperUtil.getInstance().map(request, RegisterUseCase.Input.class);
    registerUseCase.handle(input);
    return Response.success();
  }

  @GetMapping("/profile")
  public Response<GetProfileUseCase.Output> getProfile() throws Exception {
    GetProfileUseCase.Output output = getProfileUseCase.handle(null);
    return Response.success(output);
  }
}
