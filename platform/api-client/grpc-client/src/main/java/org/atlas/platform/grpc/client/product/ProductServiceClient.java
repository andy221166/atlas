package org.atlas.platform.grpc.client.product;

import java.math.BigDecimal;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.atlas.platform.api.client.contract.product.IProductServiceClient;
import org.atlas.platform.grpc.protobuf.product.ListProductBulkRequestProto;
import org.atlas.platform.grpc.protobuf.product.ListProductBulkResponseProto;
import org.atlas.platform.grpc.protobuf.product.ListProductRequestProto;
import org.atlas.platform.grpc.protobuf.product.ProductListProto;
import org.atlas.platform.grpc.protobuf.product.ProductProto;
import org.atlas.platform.grpc.protobuf.product.ProductServiceGrpc;
import org.atlas.service.product.contract.model.ProductDto;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceClient implements IProductServiceClient {

  @GrpcClient("product-service")
  private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

  @Override
  public List<ProductDto> listProductBulk(List<Integer> ids) {
    ListProductBulkRequestProto requestProto = map(ids);
    ListProductBulkResponseProto responseProto =
        productServiceBlockingStub.listProductBulk(requestProto);
    return map(responseProto);
  }

  private ListProductBulkRequestProto map(List<Integer> ids) {
    return ListProductBulkRequestProto.newBuilder()
        .addAllId(ids)
        .build();
  }

  private List<ProductDto> map(ListProductBulkResponseProto responseProto) {
    return responseProto.getProductList()
        .stream()
        .map(this::map)
        .toList();
  }

  private ProductDto map(ProductProto productProto) {
    ProductDto productDto = new ProductDto();
    productDto.setId(productProto.getId());
    productDto.setName(productProto.getName());
    productDto.setPrice(BigDecimal.valueOf(productProto.getPrice()));
    return productDto;
  }
}
