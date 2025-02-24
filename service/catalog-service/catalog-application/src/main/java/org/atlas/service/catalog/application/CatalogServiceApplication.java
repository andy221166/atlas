package org.atlas.service.catalog.application;

import org.atlas.platform.configloader.ConfigLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = {
    "org.atlas.service.catalog",
    "org.atlas.platform"
})
public class CatalogServiceApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(CatalogServiceApplication.class)
        .initializers(new ConfigLoader()).run(args);
  }
}
