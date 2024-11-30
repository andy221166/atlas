package org.atlas.service.order.contract.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.atlas.platform.cqrs.model.Command;

@Data
public class PlaceOrderCommand implements Command<Integer> {

  @Data
  public static class OrderItem {

    @NotNull
    private Integer productId;

    @NotNull
    @Min(0)
    private Integer quantity;
  }

  @NotEmpty
  private List<@Valid OrderItem> orderItems;
}
