package org.atlas.platform.rest.server.core.response;

import lombok.Data;

@Data
public class RestResponse<T> {

  private boolean success;
  private T data;
  private Integer code;
  private String message;

  public static <T> RestResponse<T> success() {
    return success(null);
  }

  public static <T> RestResponse<T> success(T data) {
    RestResponse<T> instance = new RestResponse<>();
    instance.setSuccess(true);
    instance.setData(data);
    return instance;
  }

  public static RestResponse<Void> error(int code, String message) {
    RestResponse<Void> instance = new RestResponse<>();
    instance.setSuccess(false);
    instance.setCode(code);
    instance.setMessage(message);
    return instance;
  }
}
