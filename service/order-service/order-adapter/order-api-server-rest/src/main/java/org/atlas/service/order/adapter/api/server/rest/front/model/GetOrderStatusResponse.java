package org.atlas.service.order.adapter.api.server.rest.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.service.order.domain.shared.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object containing the status of an order.")
public class GetOrderStatusResponse {

  @Schema(description = "Current status of the order.")
  private OrderStatus status;
}
