package org.atlas.infrastructure.api.server.rest.adapter.user.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserInput;
import org.atlas.domain.user.usecase.admin.AdminListUserUseCaseHandler.ListUserOutput;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.infrastructure.api.server.rest.adapter.user.admin.model.ListUserResponse;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
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
  public Response<ListUserResponse> listUser(
      @Parameter(description = "The page number to retrieve.", example = "1")
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(description = "The number of users per page.", example = "20")
      @RequestParam(value = "size", required = false, defaultValue = CommonConstant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListUserInput input = new ListUserInput(PagingRequest.of(page, size));
    ListUserOutput output = adminListUserUseCaseHandler.handle(input);
    ListUserResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListUserResponse.class);
    return Response.success(response);
  }
}
