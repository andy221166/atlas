package org.atlas.service.user.adapter.api.server.rest.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.user.adapter.api.server.rest.admin.model.ListUserResponse;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase.ListUserInput;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase.ListUserOutput;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminUserController")
@RequestMapping("api/admin/users")
@Validated
@RequiredArgsConstructor
public class UserController {

  private final ListUserUseCase listUserUseCase;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "List users", description = "Retrieves a paginated list of users.")
  public Response<ListUserResponse> listUser(
      @Parameter(description = "The page number to retrieve.", example = "1")
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(description = "The number of users per page.", example = "20")
      @RequestParam(value = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE_STR) Integer size
  ) throws Exception {
    ListUserInput input = new ListUserInput(PagingRequest.of(page, size));
    ListUserOutput output = listUserUseCase.handle(input);
    ListUserResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListUserResponse.class);
    return Response.success(response);
  }
}
