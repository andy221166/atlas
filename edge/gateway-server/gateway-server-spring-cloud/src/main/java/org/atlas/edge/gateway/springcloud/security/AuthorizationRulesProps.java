package org.atlas.edge.gateway.springcloud.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.gateway.authorization-rules")
@Data
public class AuthorizationRulesProps {

  @Data
  public static class AuthorizationRule {

    private String path;
    private List<Role> roles;
  }

  private List<String> nonSecuredPaths;
  private List<AuthorizationRule> securedPaths;

  public Map<String, List<Role>> getSecuredPathsMap() {
    return securedPaths.stream()
        .collect(Collectors.toMap(AuthorizationRule::getPath, AuthorizationRule::getRoles));
  }
}
