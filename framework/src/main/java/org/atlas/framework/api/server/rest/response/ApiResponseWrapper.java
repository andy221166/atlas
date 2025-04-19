package org.atlas.framework.api.server.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseWrapper<T> {

  private boolean success;
  private T data;
  private Integer errorCode;
  private String errorMessage;

  public static <T> ApiResponseWrapper<T> success() {
    return success(null);
  }

  public static <T> ApiResponseWrapper<T> success(T data) {
    ApiResponseWrapper<T> instance = new ApiResponseWrapper<>();
    instance.setSuccess(true);
    instance.setData(data);
    return instance;
  }

  public static ApiResponseWrapper<Void> error(int errorCode, String errorMessage) {
    ApiResponseWrapper<Void> instance = new ApiResponseWrapper<>();
    instance.setSuccess(false);
    instance.setErrorCode(errorCode);
    instance.setErrorMessage(errorMessage);
    return instance;
  }
}
