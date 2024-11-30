package org.atlas.commons.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppError {

  // Common errors
  DEFAULT(1000, "error.common.default"),
  UNAVAILABLE_SERVICE(1001, "error.common.unavailable_service"),
  BAD_REQUEST(1002, "error.common.bad_request"),
  UNAUTHORIZED(1004, "error.common.unauthorized"),
  PERMISSION_DENIED(1005, "error.common.permission_denied"),

  // User-related errors
  USER_NOT_FOUND(2000, "error.user.not_found"),
  USERNAME_ALREADY_REGISTERED(2000, "error.user.username_already_registered"),
  EMAIL_ALREADY_REGISTERED(2000, "error.user.email_already_registered"),

  // Product-related errors
  PRODUCT_NOT_FOUND(3000, "error.product.not_found"),

  // Order-related errors
  ORDER_NOT_FOUND(4000, "error.order.not_found"),
  ORDER_INVALID_STATUS(4001, "error.order.invalid_status"),
  ;

  private final int errorCode;
  private final String messageCode;

  @Override
  public String toString() {
    return String.format("%d %s", errorCode, name());
  }
}
