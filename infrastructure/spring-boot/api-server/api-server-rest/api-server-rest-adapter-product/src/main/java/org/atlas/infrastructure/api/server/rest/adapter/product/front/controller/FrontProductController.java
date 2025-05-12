package org.atlas.infrastructure.api.server.rest.adapter.product.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.usecase.front.handler.FrontGetProductUseCaseHandler;
import org.atlas.domain.product.usecase.front.handler.FrontSearchProductUseCaseHandler;
import org.atlas.domain.product.usecase.front.model.FrontSearchProductInput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.FrontSearchProductRequest;
import org.atlas.infrastructure.api.server.rest.adapter.product.shared.ProductResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/front/products")
@Validated
@RequiredArgsConstructor
public class FrontProductController {

  private final FrontSearchProductUseCaseHandler frontSearchProductUseCaseHandler;
  private final FrontGetProductUseCaseHandler frontGetProductUseCaseHandler;

  @Operation(summary = "Search for products based on various filters.")
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<List<ProductResponse>> searchProduct(
      @Parameter(description = "Request object containing search criteria.", required = true)
      @Valid @RequestBody FrontSearchProductRequest request)
      throws Exception {
    FrontSearchProductInput input = ObjectMapperUtil.getInstance()
        .map(request, FrontSearchProductInput.class);
    input.setPagingRequest(PagingRequest.of(request.getPage() - 1, request.getSize()));
    PagingResult<ProductEntity> productEntityPage = frontSearchProductUseCaseHandler.handle(input);
    List<ProductResponse> productResponses = productEntityPage.getData()
        .stream()
        .map(productEntity -> {
          ProductResponse productResponse = new ProductResponse();
          productResponse.setId(productEntity.getId());
          productResponse.setName(productEntity.getName());
          productResponse.setImage(productEntity.getImage());
          return productResponse;
        })
        .toList();
    PagingResult<ProductResponse> productResponsePage = PagingResult.of(productResponses,
        productEntityPage.getPagination());
    return ApiResponseWrapper.successPage(productResponsePage);
  }

  @Operation(summary = "Retrieve details of a specific product by ID.")
  @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<ProductResponse> getProduct(
      @Parameter(description = "The unique identifier of the product.", example = "1", required = true)
      @PathVariable("productId") Integer productId) throws Exception {
    ProductEntity productEntity = frontGetProductUseCaseHandler.handle(productId);
    ProductResponse response = ObjectMapperUtil.getInstance()
        .map(productEntity, ProductResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
