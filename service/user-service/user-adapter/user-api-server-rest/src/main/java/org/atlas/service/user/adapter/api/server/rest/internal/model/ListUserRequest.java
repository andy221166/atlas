package org.atlas.service.user.adapter.api.server.rest.internal.model;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class ListUserRequest {

  @NotEmpty
  private List<Integer> ids;
}
