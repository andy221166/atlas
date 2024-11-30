package org.atlas.service.order.contract.query;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.atlas.platform.cqrs.model.Query;
import org.atlas.service.order.domain.Order;

@Data
@AllArgsConstructor
public class GetOrderQuery implements Query<Order> {

  @NotNull
  private Integer id;
}
