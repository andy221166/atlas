package org.atlas.platform.rest.client.contract.core;

import lombok.Data;

@Data
public class Response<T> {

  private boolean success;
  private T data;
  private Integer code;
  private String message;
}
