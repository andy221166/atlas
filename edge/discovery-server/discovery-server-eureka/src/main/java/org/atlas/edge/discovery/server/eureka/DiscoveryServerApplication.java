package org.atlas.edge.discovery.server.eureka;

import org.atlas.platform.config.YamlConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.edge.discovery.server.eureka",
    "org.atlas.platform"
})
public class DiscoveryServerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(DiscoveryServerApplication.class)
        .initializers(new YamlConfigLoader()).run(args);
  }
}
