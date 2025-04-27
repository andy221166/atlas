package org.atlas.edge.auth.springsecurityjwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseLoginRequest {

  @NotBlank
  @Schema(description = "Unique identifier for the device from which the user is logging in.", example = "device123")
  private String deviceId;
}
