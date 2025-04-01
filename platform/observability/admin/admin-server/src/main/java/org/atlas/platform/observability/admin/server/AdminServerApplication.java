package org.atlas.platform.observability.admin.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.atlas.platform.config.YamlConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.platform"
})
@EnableAdminServer
public class AdminServerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(AdminServerApplication.class)
        .initializers(new YamlConfigLoader()).run(args);
  }
}
