package org.atlas.edge.config.server.springcloudconfig;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication(scanBasePackages = {
    "org.atlas"
})
@EnableConfigServer
public class ConfigServerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(DiscoveryServerApplication.class)
        .initializers(new YamlConfigLoader()).run(args);
  }
}
