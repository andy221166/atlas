package org.atlas.edge.auth.springsecurityjwt.controller;

import java.security.interfaces.RSAKey;
import java.util.Map;
import org.atlas.framework.jwt.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/.well-known/jwks.json"))
public class JwkSetController {

    private final RSAKey rsaKey;

    public JwkSetController(JwtService jwtService) {
      this.rsaKey = jwtService.getPublicKey();
    }

    @GetMapping
    public Map<String, Object> getJwks() {
        return new JWKSet(rsaKey.toPublicJWK()).toJSONObject();
    }
}
