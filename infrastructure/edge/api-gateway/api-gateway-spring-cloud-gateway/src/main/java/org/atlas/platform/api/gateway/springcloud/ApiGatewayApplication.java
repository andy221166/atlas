package org.atlas.platform.api.gateway.springcloud;

import org.atlas.platform.config.YamlConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.platform"
})
public class ApiGatewayApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ApiGatewayApplication.class)
        .initializers(new YamlConfigLoader()).run(args);
  }
}
