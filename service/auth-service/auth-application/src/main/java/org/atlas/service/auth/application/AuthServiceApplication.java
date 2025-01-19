package org.atlas.service.auth.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.product",
    "org.atlas.platform"
})
public class AuthServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(AuthServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
