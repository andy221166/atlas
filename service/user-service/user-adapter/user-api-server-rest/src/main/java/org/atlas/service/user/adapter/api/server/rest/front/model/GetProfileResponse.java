package org.atlas.service.user.adapter.api.server.rest.front.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileResponse {

  private String firstName;
  private String lastName;
}
