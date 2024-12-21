package org.atlas.service.report.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.report",
    "org.atlas.platform"
})
public class ReportServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ReportServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
