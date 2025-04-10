package org.atlas.edge.auth.springsecurityjwt.controller;

import lombok.RequiredArgsConstructor;
import org.atlas.edge.auth.springsecurityjwt.service.TokenService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/token")
@Validated
@RequiredArgsConstructor
public class TokenController {

  private final TokenService tokenService;
}
