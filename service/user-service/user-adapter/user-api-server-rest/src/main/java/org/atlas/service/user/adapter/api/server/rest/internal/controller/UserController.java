package org.atlas.service.user.adapter.api.server.rest.internal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.internal.model.ListUserRequest;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("internalUserController")
@RequestMapping("api/internal/users")
@Validated
@RequiredArgsConstructor
public class UserController {

  private final ListUserUseCase listUserUseCase;

  @PostMapping("/list")
  public Response<ListUserUseCase.Output> listUser(@Valid @RequestBody ListUserRequest request)
      throws Exception {
    ListUserUseCase.Input input =
        ObjectMapperUtil.getInstance().map(request, ListUserUseCase.Input.class);
    ListUserUseCase.Output output = listUserUseCase.handle(input);
    return Response.success(output);
  }
}
