package org.atlas.service.user.adapter.api.server.rest.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.internal.model.ListUserRequest;
import org.atlas.service.user.adapter.api.server.rest.internal.model.ListUserResponse;
import org.atlas.port.inbound.user.internal.ListUserUseCase;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserInput;
import org.atlas.port.inbound.user.internal.ListUserUseCase.ListUserOutput;
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
  @Operation(summary = "List Users", description = "Retrieves a list of users based on the provided user IDs.")
  public Response<ListUserResponse> listUser(
      @Parameter(description = "Request object containing the user IDs for the user list.", required = true)
      @Valid @RequestBody ListUserRequest request)
      throws Exception {
    ListUserInput input = ObjectMapperUtil.getInstance()
        .map(request, ListUserInput.class);
    ListUserOutput output = listUserUseCase.handle(input);
    ListUserResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListUserResponse.class);
    return Response.success(response);
  }
}
