package org.atlas.platform.api.client.rest.feign.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.api.client.contract.order.IOrderServiceClient;
import org.atlas.platform.rest.client.contract.order.ListOrderResponse;
import org.atlas.platform.api.client.rest.feign.core.OrderFeignClient;
import org.atlas.service.order.contract.dto.OrderDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceClient implements IOrderServiceClient {

  private final OrderFeignClient feignClient;

  @Override
  public PageDto<OrderDto> listOrder(Integer page, Integer size) {
    ListOrderResponse response = feignClient.listOrder(page, size);
    return response.getData();
  }
}
