package org.atlas.service.user.adapter.api.server.rest.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.front.model.GetProfileResponse;
import org.atlas.service.user.adapter.api.server.rest.front.model.RegisterRequest;
import org.atlas.service.user.port.inbound.front.GetProfileUseCase;
import org.atlas.service.user.port.inbound.front.GetProfileUseCase.GetProfileOutput;
import org.atlas.service.user.port.inbound.front.RegisterUseCase;
import org.atlas.service.user.port.inbound.front.RegisterUseCase.RegisterInput;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontUserController")
@RequestMapping("api/front/users")
@Validated
@RequiredArgsConstructor
public class UserController {

  private final RegisterUseCase registerUseCase;
  private final GetProfileUseCase getProfileUseCase;

  @Operation(summary = "User Registration", description = "Registers a new user with the provided details.")
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> register(
      @Parameter(description = "Request object containing the needed information to register a user.", required = true)
      @Valid @RequestBody RegisterRequest request) throws Exception {
    RegisterInput input = ObjectMapperUtil.getInstance()
        .map(request, RegisterInput.class);
    registerUseCase.handle(input);
    return Response.success();
  }

  @Operation(summary = "Get User Profile", description = "Retrieves the profile information of the authenticated user.")
  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<GetProfileResponse> getProfile() throws Exception {
    GetProfileOutput output = getProfileUseCase.handle(null);
    GetProfileResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProfileResponse.class);
    return Response.success(response);
  }
}
