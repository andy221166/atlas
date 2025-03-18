package org.atlas.service.user.adapter.api.server.rest.admin.controller;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.user.port.inbound.usecase.admin.ListUserUseCase;
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

  @GetMapping
  public Response<ListUserUseCase.Output> listUser(
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer size)
      throws Exception {
    ListUserUseCase.Input input = new ListUserUseCase.Input();
    input.setKeyword(keyword);
    input.setPagingRequest(PagingRequest.of(page, size));
    ListUserUseCase.Output output = listUserUseCase.handle(input);
    return Response.success(output);
  }
}
