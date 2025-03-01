package org.atlas.service.product.adapter.api.server.grpc.netdevh;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.api.server.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ListProductResponseProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ProductProto;
import org.atlas.platform.api.server.grpc.protobuf.product.ProductServiceGrpc;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

  private final ListProductUseCase listProductUseCase;

  @Override
  public void listProduct(ListProductRequestProto requestProto,
      StreamObserver<ListProductResponseProto> responseObserver) {
    ListProductUseCase.Input input = map(requestProto);
    try {
      ListProductUseCase.Output output = listProductUseCase.handle(input);
      ListProductResponseProto responseProto = map(output);
      responseObserver.onNext(responseProto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ListProductUseCase.Input map(ListProductRequestProto requestProto) {
    return new ListProductUseCase.Input(requestProto.getIdList());
  }

  private ListProductResponseProto map(ListProductUseCase.Output output) {
    if (CollectionUtils.isEmpty(output.getProducts())) {
      return ListProductResponseProto.getDefaultInstance();
    }
    ListProductResponseProto.Builder builder = ListProductResponseProto.newBuilder();
    output.getProducts().forEach(product -> builder.addProduct(map(product)));
    return builder.build();
  }

  private ProductProto map(ListProductUseCase.Output.Product product) {
    return ProductProto.newBuilder()
        .setId(product.getId())
        .setName(product.getName())
        .setPrice(product.getPrice().doubleValue())
        .build();
  }
}
