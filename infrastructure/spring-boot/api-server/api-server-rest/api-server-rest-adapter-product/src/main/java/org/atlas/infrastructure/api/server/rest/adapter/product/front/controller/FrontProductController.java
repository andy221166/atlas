package org.atlas.infrastructure.api.server.rest.adapter.product.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler.GetProductInput;
import org.atlas.domain.product.usecase.front.FrontGetProductUseCaseHandler.GetProductOutput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductInput;
import org.atlas.domain.product.usecase.front.FrontSearchProductUseCaseHandler.SearchProductOutput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.GetProductResponse;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.SearchProductRequest;
import org.atlas.infrastructure.api.server.rest.adapter.product.front.model.SearchProductResponse;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
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
  public Response<SearchProductResponse> searchProduct(
      @Parameter(description = "Request object containing search criteria.", required = true)
      @Valid @RequestBody SearchProductRequest request)
      throws Exception {
    SearchProductInput input = ObjectMapperUtil.getInstance()
        .map(request, SearchProductInput.class);
    input.setPagingRequest(PagingRequest.of(request.getPage() - 1, request.getSize()));
    SearchProductOutput output = frontSearchProductUseCaseHandler.handle(input);
    SearchProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, SearchProductResponse.class);
    return Response.success(response);
  }

  @Operation(summary = "Retrieve details of a specific product by ID.")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<GetProductResponse> getProduct(
      @Parameter(description = "The unique identifier of the product.", example = "1", required = true)
      @PathVariable("id") Integer id) throws Exception {
    GetProductInput input = new GetProductInput(id);
    GetProductOutput output = frontGetProductUseCaseHandler.handle(input);
    GetProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProductResponse.class);
    return Response.success(response);
  }
}
