package org.atlas.service.aggregator.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.aggregator",
    "org.atlas.platform"
})
public class AggregatorServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(AggregatorServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
