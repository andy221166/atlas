package org.atlas.service.report.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class User implements Serializable {

  private Integer id;
  private String firstName;
  private String lastName;
}
