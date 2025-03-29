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
public class ListCategoryResponse {

  private List<Category> categories;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Category {

    private Integer id;
    private String name;
  }
}
