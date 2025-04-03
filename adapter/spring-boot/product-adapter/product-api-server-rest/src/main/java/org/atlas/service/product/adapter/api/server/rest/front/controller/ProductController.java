package org.atlas.service.product.adapter.api.server.rest.front.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.api.server.rest.front.model.GetProductResponse;
import org.atlas.service.product.adapter.api.server.rest.front.model.SearchProductRequest;
import org.atlas.service.product.adapter.api.server.rest.front.model.SearchProductResponse;
import org.atlas.port.inbound.product.front.GetProductUseCase;
import org.atlas.port.inbound.product.front.GetProductUseCase.GetProductInput;
import org.atlas.port.inbound.product.front.GetProductUseCase.GetProductOutput;
import org.atlas.port.inbound.product.front.SearchProductUseCase;
import org.atlas.port.inbound.product.front.SearchProductUseCase.SearchProductInput;
import org.atlas.port.inbound.product.front.SearchProductUseCase.SearchProductOutput;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontProductController")
@RequestMapping("api/front/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final SearchProductUseCase searchProductUseCase;
  private final GetProductUseCase getProductUseCase;

  @Operation(summary = "Search for products based on various filters.")
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<SearchProductResponse> searchProduct(
      @Parameter(description = "Request object containing search criteria.", required = true)
      @Valid @RequestBody SearchProductRequest request)
      throws Exception {
    SearchProductInput input = ObjectMapperUtil.getInstance()
        .map(request, SearchProductInput.class);
    input.setPagingRequest(PagingRequest.of(request.getPage() - 1, request.getSize()));
    SearchProductOutput output = searchProductUseCase.handle(input);
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
    GetProductOutput output = getProductUseCase.handle(input);
    GetProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProductResponse.class);
    return Response.success(response);
  }
}
