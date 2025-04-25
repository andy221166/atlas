package org.atlas.infrastructure.api.server.rest.adapter.user.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.common.GetProfileUseCaseHandler;
import org.atlas.domain.user.usecase.common.GetProfileUseCaseHandler.GetProfileOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.user.common.model.GetProfileResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/common/users/profile")
@Validated
@RequiredArgsConstructor
public class ProfileController {

  private final GetProfileUseCaseHandler getProfileUseCaseHandler;

  @Operation(
      summary = "Get User Profile",
      description = "Retrieves the profile information of the authenticated user."
  )
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<GetProfileResponse> getProfile() throws Exception {
    GetProfileOutput output = getProfileUseCaseHandler.handle(null);
    GetProfileResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProfileResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
