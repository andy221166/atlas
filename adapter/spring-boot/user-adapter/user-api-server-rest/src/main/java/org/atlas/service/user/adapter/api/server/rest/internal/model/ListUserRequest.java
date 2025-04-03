package org.atlas.service.user.adapter.api.server.rest.internal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Request object for listing users by their IDs.")
public class ListUserRequest {

  @NotEmpty
  @Schema(description = "List of user IDs to retrieve.", example = "[1,2,3,4]")
  private List<Integer> ids;
}
