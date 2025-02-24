package org.atlas.edge.gateway.server.springcloud;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.edge.gateway.server.springcloud",
    "org.atlas.platform"
})
public class GatewayServerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(GatewayServerApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
