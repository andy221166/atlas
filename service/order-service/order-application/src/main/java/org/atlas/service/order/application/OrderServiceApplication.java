package org.atlas.service.order.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.order",
    "org.atlas.platform"
})
public class OrderServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(OrderServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
