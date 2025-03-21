package org.atlas.service.product.adapter.api.server.rest.front.controller;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.product.port.inbound.usecase.front.SearchProductUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontProductController")
@RequestMapping("api/front/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final SearchProductUseCase searchProductUseCase;

  @GetMapping
  public Response<SearchProductUseCase.Output> searchProduct(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
      @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
      @RequestParam(name = "brand_id", required = false) Integer brandId,
      @RequestParam(name = "category_ids", required = false) List<Integer> categoryIds,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer size
  ) throws Exception {
    SearchProductUseCase.Input input = SearchProductUseCase.Input.builder()
        .keyword(keyword)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .brandId(brandId)
        .categoryIds(categoryIds)
        .pagingRequest(PagingRequest.of(page - 1, size))
        .build();
    SearchProductUseCase.Output output = searchProductUseCase.handle(input);
    return Response.success(output);
  }
}
