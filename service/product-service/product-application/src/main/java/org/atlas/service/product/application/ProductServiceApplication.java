package org.atlas.service.product.application;

import org.atlas.platform.config.YamlConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.product",
    "org.atlas.platform"
})
public class ProductServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ProductServiceApplication.class)
        .initializers(new YamlConfigLoader()).run(args);
  }
}
