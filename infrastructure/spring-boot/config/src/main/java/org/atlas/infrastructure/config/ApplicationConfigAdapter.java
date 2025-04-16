package org.atlas.infrastructure.config;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.shared.enums.DecreaseQuantityStrategy;
import org.atlas.framework.config.ApplicationConfigPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationConfigAdapter implements ApplicationConfigPort {

  private final Environment environment;

  // System
  // -----------------------------------------------------------------------------------------------

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${app.system.locale:en-US}")
  private String locale;

  @Override
  public String getApplicationName() {
    return applicationName;
  }

  @Override
  public String getProfile() {
    return environment.getActiveProfiles()[0];
  }

  @Override
  public Locale getLocale() {
    return Locale.forLanguageTag(locale);
  }

  // Product service
  // -----------------------------------------------------------------------------------------------

  @Value("${app.product-service.cache.product:product}")
  private String productCacheName;

  @Value("${app.product-service.storage.bucket.product-import}")
  private String productImportBucket;

  @Value("${app.product-service.decrease-quantity-strategy:constraint}")
  private String decreaseQuantityStrategy;

  @Override
  public String getProductCacheName() {
    return productCacheName;
  }

  @Override
  public String getProductImportBucket() {
    return productImportBucket;
  }

  @Override
  public DecreaseQuantityStrategy getDecreaseQuantityStrategy() {
    return DecreaseQuantityStrategy.fromValue(decreaseQuantityStrategy);
  }

  // Notification service
  // -----------------------------------------------------------------------------------------------

  @Value("${app.notification-service.email.sender}")
  private String emailSender;

  @Override
  public String getEmailSender() {
    return emailSender;
  }
}
