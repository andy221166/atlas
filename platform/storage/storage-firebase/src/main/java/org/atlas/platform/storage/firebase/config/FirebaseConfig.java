package org.atlas.platform.storage.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {

  private final FirebaseProperties properties;

  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    FirebaseOptions options;

    // Automatically closes the InputStream after the try block
    try (InputStream credentialStream = new ClassPathResource(
        properties.getCredentials()).getInputStream()) {
      options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(credentialStream))
          .setStorageBucket(properties.getBucket())
          .build();
    }

    FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
    log.info("Initialized FirebaseApp");
    return firebaseApp;
  }

  @Bean
  @DependsOn("firebaseApp")
  public StorageClient storageClient() {
    return StorageClient.getInstance();
  }

  @PreDestroy
  public void destroy() {
    FirebaseApp.getInstance().delete();
    log.info("Destroyed FirebaseApp");
  }
}
