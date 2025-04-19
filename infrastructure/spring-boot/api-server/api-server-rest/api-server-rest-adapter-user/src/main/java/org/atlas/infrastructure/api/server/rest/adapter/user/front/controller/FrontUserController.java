package org.atlas.infrastructure.api.server.rest.adapter.user.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.front.FrontGetProfileUseCaseHandler;
import org.atlas.domain.user.usecase.front.FrontGetProfileUseCaseHandler.GetProfileOutput;
import org.atlas.domain.user.usecase.front.FrontRegisterUseCaseHandler;
import org.atlas.domain.user.usecase.front.FrontRegisterUseCaseHandler.RegisterInput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.user.front.model.GetProfileResponse;
import org.atlas.infrastructure.api.server.rest.adapter.user.front.model.RegisterRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/front/users")
@Validated
@RequiredArgsConstructor
public class FrontUserController {

  private final FrontRegisterUseCaseHandler frontRegisterUseCaseHandler;
  private final FrontGetProfileUseCaseHandler frontGetProfileUseCaseHandler;

  @Operation(summary = "User Registration", description = "Registers a new user with the provided details.")
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<Void> register(
      @Parameter(description = "Request object containing the needed information to register a user.", required = true)
      @Valid @RequestBody RegisterRequest request) throws Exception {
    RegisterInput input = ObjectMapperUtil.getInstance()
        .map(request, RegisterInput.class);
    frontRegisterUseCaseHandler.handle(input);
    return ApiResponseWrapper.success();
  }

  @Operation(summary = "Get User Profile", description = "Retrieves the profile information of the authenticated user.")
  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<GetProfileResponse> getProfile() throws Exception {
    GetProfileOutput output = frontGetProfileUseCaseHandler.handle(null);
    GetProfileResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProfileResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
