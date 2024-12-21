package org.atlas.service.task.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.task",
    "org.atlas.platform"
})
public class TaskServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(TaskServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
