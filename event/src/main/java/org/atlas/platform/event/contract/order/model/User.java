package org.atlas.platform.event.contract.order.model;

import lombok.Data;

@Data
public class User {

  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
}
