package org.atlas.service.user.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.user",
    "org.atlas.platform"
})
public class UserServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(UserServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
