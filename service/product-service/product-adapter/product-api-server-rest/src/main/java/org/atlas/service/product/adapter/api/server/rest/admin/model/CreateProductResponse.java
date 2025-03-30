package org.atlas.service.product.adapter.api.server.rest.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for creating a new product.")
public class CreateProductResponse {

  @Schema(description = "Unique identifier of the created product.", example = "123")
  private Integer id;
}
