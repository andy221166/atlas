package org.atlas.infrastructure.internalapi.product.grpc.netdevh;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;
import org.atlas.framework.internalapi.ProductApiPort;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ListProductResponseProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ProductProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ProductServiceGrpc;
import org.springframework.stereotype.Component;

@Component
@Retry(name = "default")
@CircuitBreaker(name = "default")
@Bulkhead(name = "default")
public class ProductApiAdapter implements ProductApiPort {

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
