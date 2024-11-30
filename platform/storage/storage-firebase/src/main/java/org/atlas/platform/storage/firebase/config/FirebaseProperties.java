package org.atlas.platform.storage.firebase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.firebase")
@Data
public class FirebaseProperties {

  private String credentials;
  private String bucket;
}
