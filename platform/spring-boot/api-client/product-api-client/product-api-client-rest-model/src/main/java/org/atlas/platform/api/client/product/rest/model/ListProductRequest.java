package org.atlas.platform.api.client.product.rest.model;

import java.util.List;
import lombok.Data;

@Data
public class ListProductRequest {

  private List<Integer> ids;
}
