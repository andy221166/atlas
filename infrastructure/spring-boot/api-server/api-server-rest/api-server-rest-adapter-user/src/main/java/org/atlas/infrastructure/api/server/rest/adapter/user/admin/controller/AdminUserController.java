package org.atlas.infrastructure.api.server.rest.adapter.user.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.UserOutput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.infrastructure.api.server.rest.adapter.user.admin.model.UserResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/users")
@Validated
@RequiredArgsConstructor
public class AdminUserController {

  private final AdminListUserUseCaseHandler adminListUserUseCaseHandler;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "List users", description = "Retrieves a paginated list of users.")
  public ApiResponseWrapper<List<UserResponse>> listUser(
      @Parameter(description = "User ID", example = "1")
      @RequestParam(value = "id", required = false) Integer id,
      @Parameter(description = "Username", example = "john.doe")
      @RequestParam(value = "username", required = false) String username,
      @Parameter(description = "Email", example = "john.doe@example.com")
      @RequestParam(value = "email", required = false) String email,
      @Parameter(description = "Phone number", example = "0123456789")
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
      @Parameter(description = "The page number", example = "1")
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(description = "The number of users per page", example = "20")
      @RequestParam(value = "size", required = false, defaultValue = CommonConstant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListUserInput input = ListUserInput.builder()
        .id(id)
        .username(username)
        .email(email)
        .phoneNumber(phoneNumber)
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    PagingResult<UserOutput> output = adminListUserUseCaseHandler.handle(input);
    PagingResult<UserResponse> response = ObjectMapperUtil.getInstance()
        .mapPage(output, UserResponse.class);
    return ApiResponseWrapper.successPage(response);
  }
}
