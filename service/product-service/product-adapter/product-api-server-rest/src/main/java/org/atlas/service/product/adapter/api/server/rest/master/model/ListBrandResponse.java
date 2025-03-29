package org.atlas.service.product.adapter.api.server.rest.master.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListBrandResponse {

  private List<Brand> brands;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Brand {

    private Integer id;
    private String name;
  }
}
