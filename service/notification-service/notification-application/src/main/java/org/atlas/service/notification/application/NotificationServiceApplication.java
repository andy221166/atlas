package org.atlas.service.notification.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.notification",
    "org.atlas.platform"
})
public class NotificationServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(NotificationServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
