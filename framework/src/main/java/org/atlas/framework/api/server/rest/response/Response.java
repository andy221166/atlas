package org.atlas.framework.api.server.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

  private boolean success;
  private T data;
  private Integer errorCode;
  private String errorMessage;

  public static <T> Response<T> success() {
    return success(null);
  }

  public static <T> Response<T> success(T data) {
    Response<T> instance = new Response<>();
    instance.setSuccess(true);
    instance.setData(data);
    return instance;
  }

  public static Response<Void> error(int errorCode, String errorMessage) {
    Response<Void> instance = new Response<>();
    instance.setSuccess(false);
    instance.setErrorCode(errorCode);
    instance.setErrorMessage(errorMessage);
    return instance;
  }
}
