package org.atlas.platform.rest.client.apachehttpclient.order;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.model.PageDto;
import org.atlas.platform.api.client.contract.order.IOrderServiceClient;
import org.atlas.platform.rest.client.apachehttpclient.core.HttpClientService;
import org.atlas.platform.rest.client.contract.order.ListOrderResponse;
import org.atlas.service.order.contract.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceClient implements IOrderServiceClient {

  @Value("${app.rest-client.order-service.base-url:http://localhost:8081}")
  private String baseUrl;

  private final HttpClientService service;

  @Override
  public PageDto<OrderDto> listOrder(Integer page, Integer size) {
    String url = String.format("%s/api/orders", baseUrl);

    Map<String, String> paramsMap = new HashMap<>();
    paramsMap.put("page", String.valueOf(page));
    paramsMap.put("size", String.valueOf(size));

    ListOrderResponse response = service.doGet(url, paramsMap, null, ListOrderResponse.class);
    return response.getData();
  }
}
