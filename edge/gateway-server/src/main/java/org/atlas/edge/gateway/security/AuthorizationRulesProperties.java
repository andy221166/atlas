package org.atlas.edge.gateway.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.atlas.service.user.domain.shared.enums.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.authorization-rules")
@Data
public class AuthorizationRulesProperties {

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
