package org.atlas.edge.discovery.eureka;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.edge.discovery.eureka",
    "org.atlas.platform"
})
@EnableEurekaServer
public class EurekaServerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(EurekaServerApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
