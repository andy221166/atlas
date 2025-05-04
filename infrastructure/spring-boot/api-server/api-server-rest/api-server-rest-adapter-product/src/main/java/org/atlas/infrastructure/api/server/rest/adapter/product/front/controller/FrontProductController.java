package org.atlas.infrastructure.api.server.rest.adapter.product.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler.GetProductInput;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler.GetProductOutput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.ProductOutput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductInput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.DetailedProductResponse;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.ProductResponse;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.SearchProductRequest;
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
      @Valid @RequestBody SearchProductRequest request)
      throws Exception {
    SearchProductInput input = ObjectMapperUtil.getInstance()
        .map(request, SearchProductInput.class);
    input.setPagingRequest(PagingRequest.of(request.getPage() - 1, request.getSize()));
    PagingResult<ProductOutput> output = frontSearchProductUseCaseHandler.handle(input);
    PagingResult<ProductResponse> response = ObjectMapperUtil.getInstance()
        .mapPage(output, ProductResponse.class);
    return ApiResponseWrapper.successPage(response);
  }

  @Operation(summary = "Retrieve details of a specific product by ID.")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<DetailedProductResponse> getProduct(
      @Parameter(description = "The unique identifier of the product.", example = "1", required = true)
      @PathVariable("id") Integer id) throws Exception {
    GetProductInput input = new GetProductInput(id);
    GetProductOutput output = frontGetProductUseCaseHandler.handle(input);
    DetailedProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, DetailedProductResponse.class);
    return ApiResponseWrapper.success(response);
  }
}
