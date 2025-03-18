package org.atlas.platform.api.client.product.grpc.netdevh;

import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.server.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ListProductResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ProductProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ProductServiceGrpc;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.api.client.product.ProductApiClient;
import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;
import org.springframework.stereotype.Component;

@Component
public class ProductApiClientImpl implements ProductApiClient {

  @GrpcClient("product-service")
  private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

  @Override
  public ListProductUseCase.Output call(ListProductUseCase.Input input) {
    ListProductRequestProto requestProto = map(input);
    ListProductResponseProto responseProto = productServiceBlockingStub.listProduct(requestProto);
    return map(responseProto);
  }

  private ListProductRequestProto map(ListProductUseCase.Input input) {
    return ListProductRequestProto.newBuilder()
        .addAllId(input.getIds())
        .build();
  }

  private ListProductUseCase.Output map(ListProductResponseProto responseProto) {
    List<ListProductUseCase.Output.Product> products = responseProto.getProductList()
        .stream()
        .map(this::map)
        .toList();
    return new ListProductUseCase.Output(products);
  }

  private ListProductUseCase.Output.Product map(ProductProto productProto) {
    return ObjectMapperUtil.getInstance()
        .map(productProto, ListProductUseCase.Output.Product.class);
  }
}
