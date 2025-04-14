package org.atlas.framework.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.atlas.framework.error.AppError;

@Getter
@NoArgsConstructor
public class BusinessException extends RuntimeException {

  private int errorCode;
  private String messageCode;

  public BusinessException(AppError error) {
    this.errorCode = error.getErrorCode();
    this.messageCode = error.getMessageCode();
  }

  public BusinessException(AppError error, String errorMessage) {
    super(errorMessage);
    this.errorCode = error.getErrorCode();
  }
}
