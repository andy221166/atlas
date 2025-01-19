package org.atlas.platform.grpc.server.product;

import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.platform.grpc.protobuf.product.ProductListProto;
import org.atlas.platform.grpc.protobuf.product.ProductProto;
import org.atlas.platform.grpc.protobuf.product.ProductServiceGrpc;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.ListProductBulkQuery;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

  private final QueryGateway queryGateway;

  @Override
  public void listProduct(ListProductRequestProto requestProto,
      StreamObserver<ProductListProto> responseObserver) {
    ListProductBulkQuery command = map(requestProto);
    try {
      List<ProductDto> productDtoList = queryGateway.send(command);
      ProductListProto responseProto = map(productDtoList);
      responseObserver.onNext(responseProto);
      responseObserver.onCompleted();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ListProductBulkQuery map(ListProductRequestProto requestProto) {
    return new ListProductBulkQuery(requestProto.getIdList());
  }

  private ProductProto map(ProductDto productDto) {
    return ProductProto.newBuilder()
        .setId(productDto.getId())
        .setName(productDto.getName())
        .setPrice(productDto.getPrice().doubleValue())
        .build();
  }

  private ProductListProto map(List<ProductDto> productDtoList) {
    if (CollectionUtils.isEmpty(productDtoList)) {
      return ProductListProto.getDefaultInstance();
    }
    ProductListProto.Builder builder = ProductListProto.newBuilder();
    productDtoList.forEach(productDto -> builder.addProduct(map(productDto)));
    return builder.build();
  }
}
