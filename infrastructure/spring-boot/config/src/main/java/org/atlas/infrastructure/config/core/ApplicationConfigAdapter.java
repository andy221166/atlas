package org.atlas.infrastructure.config.core;

import org.atlas.domain.product.shared.enums.DecreaseQuantityStrategy;
import org.atlas.framework.config.ApplicationConfigPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfigAdapter implements ApplicationConfigPort {

  // Commons
  // -----------------------------------------------------------------------------------------------

  @Value("${spring.application.name}")
  private String applicationName;

  @Override
  public String getApplicationName() {
    return applicationName;
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
    return DecreaseQuantityStrategy.valueOf(decreaseQuantityStrategy);
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
