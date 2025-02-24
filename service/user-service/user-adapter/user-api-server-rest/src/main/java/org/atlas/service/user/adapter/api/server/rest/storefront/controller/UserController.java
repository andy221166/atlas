package org.atlas.service.user.adapter.api.server.rest.storefront.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.usecase.port.input.EmptyInput;
import org.atlas.service.user.adapter.api.server.rest.storefront.model.RegisterRequest;
import org.atlas.service.user.port.inbound.storefront.GetProfileUseCase;
import org.atlas.service.user.port.inbound.storefront.RegisterUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("storefrontUserController")
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
    EmptyInput input = EmptyInput.getInstance();
    GetProfileUseCase.Output output = getProfileUseCase.handle(input);
    return Response.success(output);
  }
}
