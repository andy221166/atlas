package org.atlas.platform.api.client.product.grpc.netdevh;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.platform.api.server.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ListProductResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ProductProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ProductServiceGrpc;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductInput;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductOutput;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default-internal")
@CircuitBreaker(name = "default-internal")
@Bulkhead(name = "default-internal")
public class ProductApiClientImpl implements ProductApiClient {

  @GrpcClient("product-service")
  private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

  @Override
  public ListProductOutput call(ListProductInput input) {
    ListProductRequestProto requestProto = map(input);
    ListProductResponseProto responseProto = productServiceBlockingStub.listProduct(requestProto);
    return map(responseProto);
  }

  private ListProductRequestProto map(ListProductInput input) {
    return ListProductRequestProto.newBuilder()
        .addAllId(input.getIds())
        .build();
  }

  private ListProductOutput map(ListProductResponseProto responseProto) {
    List<ListProductOutput.Product> products = responseProto.getProductList()
        .stream()
        .map(this::map)
        .toList();
    return new ListProductOutput(products);
  }

  private ListProductOutput.Product map(ProductProto productProto) {
    return ObjectMapperUtil.getInstance()
        .map(productProto, ListProductOutput.Product.class);
  }
}
