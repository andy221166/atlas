package org.atlas.infrastructure.api.server.rest.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

  private boolean success;
  private T data;
  private Integer code;
  private String message;

  public static <T> Response<T> success() {
    return success(null);
  }

  public static <T> Response<T> success(T data) {
    Response<T> instance = new Response<>();
    instance.setSuccess(true);
    instance.setData(data);
    return instance;
  }

  public static Response<Void> error(int code, String message) {
    Response<Void> instance = new Response<>();
    instance.setSuccess(false);
    instance.setCode(code);
    instance.setMessage(message);
    return instance;
  }
}
