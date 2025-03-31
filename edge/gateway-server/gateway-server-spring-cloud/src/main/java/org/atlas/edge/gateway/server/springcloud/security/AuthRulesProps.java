package org.atlas.edge.gateway.server.springcloud.security;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.atlas.platform.commons.enums.Role;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.gateway.auth-rules")
@Data
public class AuthRulesProps {

  @Data
  public static class AuthRule {

    private String path;
    private List<Role> roles;
  }

  private List<String> nonSecuredPaths;
  private List<AuthRule> securedPaths;

  public Map<String, List<Role>> getSecuredPathsMap() {
    return securedPaths.stream()
        .collect(Collectors.toMap(AuthRule::getPath, AuthRule::getRoles));
  }
}
