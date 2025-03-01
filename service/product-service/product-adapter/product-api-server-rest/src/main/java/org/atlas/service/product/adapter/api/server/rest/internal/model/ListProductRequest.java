package org.atlas.service.product.adapter.api.server.rest.internal.model;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class ListProductRequest {

  @NotEmpty
  private List<Integer> ids;
}
