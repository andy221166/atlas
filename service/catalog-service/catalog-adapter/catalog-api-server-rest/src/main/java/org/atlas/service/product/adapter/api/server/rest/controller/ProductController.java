package org.atlas.service.product.adapter.api.server.rest.controller;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.paging.PageDto;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.cqrs.command.CommandGateway;
import org.atlas.platform.cqrs.query.QueryGateway;
import org.atlas.platform.rest.server.core.response.RestResponse;
import org.atlas.service.product.contract.command.ImportProductCommand;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.ListProductQuery;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final QueryGateway queryGateway;
  private final CommandGateway commandGateway;

  @GetMapping
  public RestResponse<List<ProductDto>> listProduct(
      @RequestParam(name = "ids", required = false) List<Integer> ids) throws Exception {
    ListProductQuery command = new ListProductQuery(ids);
    return RestResponse.success(queryGateway.send(command));
  }

  @GetMapping("/search")
  public RestResponse<PageDto<ProductDto>> searchProduct(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
      @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
      @RequestParam(name = "category_id", required = false) Integer categoryId,
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
  ) throws Exception {
    SearchProductQuery query = new SearchProductQuery();
    query.setKeyword(keyword);
    query.setMinPrice(minPrice);
    query.setMaxPrice(maxPrice);
    query.setCategoryId(categoryId);
    query.setPage(page - 1);
    query.setSize(size);
    return RestResponse.success(queryGateway.send(query));
  }

  @PostMapping("/import")
  public RestResponse<Integer> importProduct(@RequestParam("file") MultipartFile file,
      @RequestParam("fileType") FileType fileType) throws Exception {
    byte[] fileContent = file.getBytes();
    ImportProductCommand command = new ImportProductCommand(fileType, fileContent);
    return RestResponse.success(commandGateway.send(command));
  }
}
