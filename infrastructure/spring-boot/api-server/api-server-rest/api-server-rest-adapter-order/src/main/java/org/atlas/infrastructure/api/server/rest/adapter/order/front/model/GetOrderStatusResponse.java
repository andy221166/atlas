package org.atlas.infrastructure.api.server.rest.adapter.order.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.domain.order.shared.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ApiResponseWrapper object containing the status of an order.")
public class GetOrderStatusResponse {

  @Schema(description = "Current status of the order.")
  private OrderStatus status;
}
