package org.atlas.framework.exception;

import lombok.Getter;
import org.atlas.framework.error.AppError;

@Getter
public class BusinessException extends RuntimeException {

  private int errorCode;
  private String messageCode;

  public BusinessException(AppError error) {
    super(error != null ? error.toString() : "Unknown error");
    if (error != null) {
      this.errorCode = error.getErrorCode();
      this.messageCode = error.getMessageCode();
    }
  }

}
