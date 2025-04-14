package org.atlas.framework.config;

import org.atlas.domain.product.shared.enums.DecreaseQuantityStrategy;

public interface ApplicationConfigPort {

  // System
  String getApplicationName();
  String getProfile();

  // Product
  String getProductCacheName();
  String getProductImportBucket();
  DecreaseQuantityStrategy getDecreaseQuantityStrategy();

  // Notification
  String getEmailSender();
}
