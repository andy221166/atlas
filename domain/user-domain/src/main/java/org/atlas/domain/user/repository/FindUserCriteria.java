package org.atlas.domain.user.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUserCriteria {

  private Integer id;
  private String username;
  private String email;
  private String phoneNumber;
}
