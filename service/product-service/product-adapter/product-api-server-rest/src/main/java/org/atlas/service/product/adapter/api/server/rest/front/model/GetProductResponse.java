package org.atlas.service.product.adapter.api.server.rest.front.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {

  private Integer id;
  private String name;
  private BigDecimal price;
  private String imageUrl;
  private String description;
  private Map<String, String> attributes;
  private String brand;
  private List<String> categories;
}
