package org.atlas.infrastructure.api.server.grpc.netdevh.adapter.product;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductInput;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductOutput;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ListProductResponseProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ProductProto;
import org.atlas.infrastructure.api.server.grpc.protobuf.product.ProductServiceGrpc;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

  private final InternalListProductUseCaseHandler internalListProductUseCaseHandler;

  @Override
  public void listProduct(ListProductRequestProto requestProto,
      StreamObserver<ListProductResponseProto> responseObserver) {
    ListProductInput input = map(requestProto);
    try {
      ListProductOutput output = internalListProductUseCaseHandler.handle(input);
      ListProductResponseProto responseProto = map(output);
      responseObserver.onNext(responseProto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ListProductInput map(ListProductRequestProto requestProto) {
    return new ListProductInput(requestProto.getIdList());
  }

  private ListProductResponseProto map(ListProductOutput output) {
    if (CollectionUtils.isEmpty(output.getProducts())) {
      return ListProductResponseProto.getDefaultInstance();
    }
    ListProductResponseProto.Builder builder = ListProductResponseProto.newBuilder();
    output.getProducts().forEach(product -> builder.addProduct(map(product)));
    return builder.build();
  }

  private ProductProto map(ListProductOutput.Product product) {
    return ProductProto.newBuilder()
        .setId(product.getId())
        .setName(product.getName())
        .setPrice(product.getPrice().doubleValue())
        .build();
  }
}
