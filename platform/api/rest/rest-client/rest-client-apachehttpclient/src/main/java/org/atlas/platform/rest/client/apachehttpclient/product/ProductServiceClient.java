package org.atlas.platform.rest.client.apachehttpclient.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.atlas.platform.api.client.contract.product.IProductServiceClient;
import org.atlas.platform.rest.client.apachehttpclient.core.HttpClientService;
import org.atlas.platform.rest.client.contract.product.ListProductResponse;
import org.atlas.service.product.contract.model.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient implements IProductServiceClient {

  @Value("${app.rest.product.base-url:http://localhost:8082}")
  private String baseUrl;

  private final HttpClientService service;

  @Override
  public List<ProductDto> listProduct(List<Integer> ids) {
    String url = String.format("%s/api/products", baseUrl);

    Map<String, String> paramsMap = new HashMap<>();
    paramsMap.put("ids", StringUtils.join(ids, ","));

    ListProductResponse response = service.doGet(url, paramsMap, null, ListProductResponse.class);
    return response.getData();
  }
}
