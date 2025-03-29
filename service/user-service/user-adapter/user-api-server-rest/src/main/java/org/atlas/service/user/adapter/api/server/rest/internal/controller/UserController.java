package org.atlas.service.user.adapter.api.server.rest.internal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.internal.model.ListUserRequest;
import org.atlas.service.user.adapter.api.server.rest.internal.model.ListUserResponse;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase.ListUserOutput;
import org.atlas.service.user.port.inbound.internal.ListUserUseCase;
import org.springframework.http.MediaType;
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

  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListUserResponse> listUser(@Valid @RequestBody ListUserRequest request)
      throws Exception {
    ListUserInput input = ObjectMapperUtil.getInstance().map(request, ListUserInput.class);
    ListUserOutput output = listUserUseCase.handle(input);
    ListUserResponse response = ObjectMapperUtil.getInstance().map(output, ListUserResponse.class);
    return Response.success(response);
  }
}
