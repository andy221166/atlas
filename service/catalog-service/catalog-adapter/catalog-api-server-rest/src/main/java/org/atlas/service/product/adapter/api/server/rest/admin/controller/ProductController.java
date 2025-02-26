package org.atlas.service.product.adapter.api.server.rest.admin.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.service.catalog.domain.entity.ProductStatus;
import org.atlas.service.catalog.port.inbound.admin.ListProductUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final ListProductUseCase listProductUseCase;

  @GetMapping
  public Response<ListProductUseCase.Output> searchProduct(
      @RequestParam(name = "id", required = false) Integer id,
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
      @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
      @RequestParam(name = "status", required = false) ProductStatus status,
      @RequestParam(name = "available_from", required = false) Date availableFrom,
      @RequestParam(name = "is_active", required = false) Boolean isActive,
      @RequestParam(name = "brand_id", required = false) Integer brandId,
      @RequestParam(name = "category_ids", required = false) List<Integer> categoryIds,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
  ) throws Exception {
    ListProductUseCase.Input input = new ListProductUseCase.Input();
    input.setId(id);
    input.setKeyword(keyword);
    input.setMinPrice(minPrice);
    input.setMaxPrice(maxPrice);
    input.setStatus(status);
    input.setAvailableFrom(availableFrom);
    input.setIsActive(isActive);
    input.setBrandId(brandId);
    input.setCategoryIds(categoryIds);
    input.setPagingRequest(PagingRequest.of(page, size));
    ListProductUseCase.Output output = listProductUseCase.handle(input);
    return Response.success(output);
  }
}
