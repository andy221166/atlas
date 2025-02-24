package org.atlas.service.user.adapter.api.server.rest.admin.controller;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.service.user.port.inbound.admin.ListUserUseCase;
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
      @RequestParam("keyword") String keyword,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) throws Exception {
    ListUserUseCase.Input input = new ListUserUseCase.Input();
    input.setKeyword(keyword);
    input.setPage(page);
    input.setSize(size);
    ListUserUseCase.Output output = listUserUseCase.handle(input);
    return Response.success(output);
  }
}
