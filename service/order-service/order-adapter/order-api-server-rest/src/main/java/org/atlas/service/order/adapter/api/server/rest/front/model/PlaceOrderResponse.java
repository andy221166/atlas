package org.atlas.service.order.adapter.api.server.rest.front.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for placing a new order.")
public class PlaceOrderResponse {

  @Schema(description = "Unique identifier of the newly created order.", example = "1")
  private Integer orderId;
}
