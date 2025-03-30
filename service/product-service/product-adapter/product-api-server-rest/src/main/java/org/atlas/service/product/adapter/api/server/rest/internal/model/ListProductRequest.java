package org.atlas.service.product.adapter.api.server.rest.internal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class ListProductRequest {

  @NotEmpty
  @Schema(description = "List of unique identifiers (IDs) for the products to be retrieved.", required = true, example = "[1, 2, 3]")
  private List<Integer> ids;
}
