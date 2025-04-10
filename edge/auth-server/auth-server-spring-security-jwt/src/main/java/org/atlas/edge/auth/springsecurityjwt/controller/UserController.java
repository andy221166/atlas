package org.atlas.edge.auth.springsecurityjwt.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.edge.auth.springsecurityjwt.model.CreateUserRequest;
import org.atlas.edge.auth.springsecurityjwt.service.UserService;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> createUser(
      @Parameter(description = "Request object containing registration data.", required = true)
      @Valid @RequestBody CreateUserRequest request) throws Exception {
    userService.createUser(request);
    return Response.success();
  }
}
