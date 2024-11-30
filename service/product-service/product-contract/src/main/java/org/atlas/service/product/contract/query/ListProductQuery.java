package org.atlas.service.product.contract.query;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.product.contract.model.ProductDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProductQuery implements Query<List<ProductDto>> {

  @NotEmpty
  private List<Integer> ids;
}
