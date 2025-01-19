package org.atlas.platform.api.client.contract.order;

import org.atlas.commons.model.PageDto;
import org.atlas.service.order.contract.dto.OrderDto;
import org.atlas.service.order.contract.query.ListOrderQuery;

public interface IOrderServiceClient {

  PageDto<OrderDto> listOrder(Integer page, Integer size);
}
