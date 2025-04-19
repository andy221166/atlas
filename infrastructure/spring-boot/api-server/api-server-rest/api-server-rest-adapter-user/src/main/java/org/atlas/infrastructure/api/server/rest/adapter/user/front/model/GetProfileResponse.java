package org.atlas.infrastructure.api.server.rest.adapter.user.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ApiResponseWrapper object containing user profile information.")
public class GetProfileResponse {

  @Schema(description = "User's first name.", example = "John")
  private String firstName;

  @Schema(description = "User's last name.", example = "Doe")
  private String lastName;
}
