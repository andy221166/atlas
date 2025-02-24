package org.atlas.platform.api.client.rest.model.internal;

import lombok.Data;

@Data
public class Response<T> {

  private boolean success;
  private T data;
  private Integer code;
  private String message;
}
