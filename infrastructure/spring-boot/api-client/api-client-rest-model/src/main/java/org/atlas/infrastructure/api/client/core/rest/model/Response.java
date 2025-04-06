package org.atlas.infrastructure.api.client.core.rest.model;

import lombok.Data;

@Data
public class Response<T> {

  private boolean success;
  private T data;
  private Integer code;
  private String message;
}
