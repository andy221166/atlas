package org.atlas.service.product.adapter.api.server.rest.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.PagingResponse;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.api.server.rest.front.model.SearchProductRequest;
import org.atlas.service.product.port.inbound.usecase.front.GetProductUseCase;
import org.atlas.service.product.port.inbound.usecase.front.SearchProductUseCase;
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

  @PostMapping
  public PagingResponse<SearchProductUseCase.Output.Product> searchProduct(
      @Valid @RequestBody SearchProductRequest request) throws Exception {
    SearchProductUseCase.Input input = ObjectMapperUtil.getInstance()
        .map(request, SearchProductUseCase.Input.class);
    input.setPagingRequest(PagingRequest.of(request.getPage() - 1, request.getSize()));
    SearchProductUseCase.Output output = searchProductUseCase.handle(input);
    return PagingResponse.success(output.getProductPage());
  }

  @GetMapping("/{id}")
  public Response<GetProductUseCase.Output> getProduct(@PathVariable("id") Integer id)
      throws Exception {
    GetProductUseCase.Input input = new GetProductUseCase.Input(id);
    GetProductUseCase.Output output = getProductUseCase.handle(input);
    return Response.success(output);
  }
}
