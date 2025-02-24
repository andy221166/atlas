package org.atlas.service.catalog.api.client.grpc.netdevh;

import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.server.grpc.protobuf.catalog.ListProductRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.catalog.ListProductResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.catalog.ProductProto;
import org.atlas.platform.api.server.grpc.protobuf.catalog.ProductServiceGrpc;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.api.client.CatalogApiClient;
import org.atlas.service.catalog.port.inbound.internal.ListProductUseCase;
import org.springframework.stereotype.Component;

@Component
public class CatalogApiClientImpl implements CatalogApiClient {

  @GrpcClient("catalog-service")
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
