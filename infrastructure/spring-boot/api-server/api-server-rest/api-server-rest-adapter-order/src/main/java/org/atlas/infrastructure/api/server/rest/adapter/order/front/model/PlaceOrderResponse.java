package org.atlas.infrastructure.api.server.rest.adapter.order.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ApiResponseWrapper object for placing a new order.")
public class PlaceOrderResponse {

  @Schema(description = "Unique identifier of the newly created order.", example = "1")
  private Integer orderId;
}
