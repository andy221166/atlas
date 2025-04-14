package org.atlas.infrastructure.api.server.rest.adapter.user.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.internal.InternalListUserUseCaseHandler;
import org.atlas.domain.user.usecase.internal.InternalListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.internal.InternalListUserUseCaseHandler.ListUserOutput;
import org.atlas.framework.api.server.rest.response.Response;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.user.internal.model.ListUserRequest;
import org.atlas.infrastructure.api.server.rest.adapter.user.internal.model.ListUserResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/internal/users")
@Validated
@RequiredArgsConstructor
public class InternalUserController {

  private final InternalListUserUseCaseHandler internalListUserUseCaseHandler;

  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "List Users", description = "Retrieves a list of users based on the provided user IDs.")
  public Response<ListUserResponse> listUser(
      @Parameter(description = "Request object containing the user IDs for the user list.", required = true)
      @Valid @RequestBody ListUserRequest request)
      throws Exception {
    ListUserInput input = ObjectMapperUtil.getInstance()
        .map(request, ListUserInput.class);
    ListUserOutput output = internalListUserUseCaseHandler.handle(input);
    ListUserResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListUserResponse.class);
    return Response.success(response);
  }
}
