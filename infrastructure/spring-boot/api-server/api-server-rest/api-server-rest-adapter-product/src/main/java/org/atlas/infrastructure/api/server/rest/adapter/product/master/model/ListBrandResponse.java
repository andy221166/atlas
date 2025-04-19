package org.atlas.infrastructure.api.server.rest.adapter.product.master.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ApiResponseWrapper object containing the list of brands.")
public class ListBrandResponse {

  @Schema(description = "List of brands retrieved from the request.", required = true)
  private List<Brand> brands;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Represents a brand in the brand list.")
  public static class Brand {

    @Schema(description = "Unique identifier of the brand.", example = "1")
    private Integer id;

    @Schema(description = "Name of the brand.", example = "Brand Name")
    private String name;
  }
}
