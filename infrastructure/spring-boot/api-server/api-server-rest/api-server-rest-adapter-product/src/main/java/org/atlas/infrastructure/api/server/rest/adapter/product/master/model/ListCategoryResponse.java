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
@Schema(description = "ApiResponseWrapper object containing the list of categories.")
public class ListCategoryResponse {

  @Schema(description = "List of categories retrieved from the request.", required = true)
  private List<Category> categories;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Represents a category in the category list.")
  public static class Category {

    @Schema(description = "Unique identifier of the category.", example = "1")
    private Integer id;

    @Schema(description = "Name of the category.", example = "Category Name")
    private String name;
  }
}
