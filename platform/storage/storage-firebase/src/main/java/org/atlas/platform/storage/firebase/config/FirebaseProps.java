package org.atlas.platform.storage.firebase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.firebase")
@Data
public class FirebaseProps {

  private String credentials;
  private String bucket;
}
