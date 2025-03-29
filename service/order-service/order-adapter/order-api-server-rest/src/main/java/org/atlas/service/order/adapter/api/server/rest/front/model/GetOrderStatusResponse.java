package org.atlas.service.order.adapter.api.server.rest.front.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.atlas.service.order.domain.shared.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderStatusResponse {

  private OrderStatus status;
}
