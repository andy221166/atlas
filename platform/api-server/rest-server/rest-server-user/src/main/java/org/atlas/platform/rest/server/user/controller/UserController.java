package org.atlas.platform.rest.server.user.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.gateway.CommandGateway;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.RestResponse;
import org.atlas.service.user.contract.command.SignInCommand;
import org.atlas.service.user.contract.command.SignOutCommand;
import org.atlas.service.user.contract.command.SignUpCommand;
import org.atlas.service.user.contract.model.UserDto;
import org.atlas.service.user.contract.query.GetUserQuery;
import org.atlas.service.user.contract.query.ListUserBulkQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;

  @GetMapping
  public RestResponse<List<UserDto>> listUser(@RequestParam("ids") List<Integer> ids)
      throws Exception {
    ListUserBulkQuery query = new ListUserBulkQuery(ids);
    return RestResponse.success(queryGateway.send(query));
  }

  @PostMapping("/sign-up")
  public RestResponse<Void> signUp(@Valid @RequestBody SignUpCommand command) throws Exception {
    return RestResponse.success(commandGateway.send(command));
  }

  @PostMapping("/sign-in")
  public RestResponse<SignInResDto> signIn(@Valid @RequestBody SignInCommand command)
      throws Exception {
    return RestResponse.success(commandGateway.send(command));
  }

  @GetMapping("/profile")
  public RestResponse<ProfileDto> getProfile() throws Exception {
    GetUserQuery query = new GetUserQuery();
    return RestResponse.success(queryGateway.send(query));
  }

  @PostMapping("/sign-out")
  public RestResponse<Void> signOut(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
    SignOutCommand command = new SignOutCommand(authorizationHeader);
    commandGateway.send(command);
    return RestResponse.success();
  }
}
