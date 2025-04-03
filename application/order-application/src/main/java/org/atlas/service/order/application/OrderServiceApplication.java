package org.atlas.service.order.application;

import org.atlas.platform.config.YamlConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.order",
    "org.atlas.platform"
})
public class OrderServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(OrderServiceApplication.class)
        .initializers(new YamlConfigLoader()).run(args);
  }
}
