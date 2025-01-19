package org.atlas.platform.rest.server.product.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.enums.FileType;
import org.atlas.commons.model.PageDto;
import org.atlas.commons.util.DateUtil;
import org.atlas.platform.cqrs.gateway.CommandGateway;
import org.atlas.platform.cqrs.gateway.QueryGateway;
import org.atlas.platform.rest.server.core.response.Response;
import org.atlas.service.product.contract.model.ImportProductResultDto;
import org.atlas.service.product.contract.model.ProductDto;
import org.atlas.service.product.contract.query.GetImportProductResultQuery;
import org.atlas.service.product.contract.query.ListProductBulkQuery;
import org.atlas.service.product.contract.query.ListProductQuery;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final QueryGateway queryGateway;
  private final CommandGateway commandGateway;

  @GetMapping
  public Response<PageDto<ProductDto>> listProduct(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") Integer size)
      throws Exception {
    ListProductQuery query = new ListProductQuery(page, size);
    PageDto<ProductDto> productPage = queryGateway.send(query);
    return Response.success(productPage);
  }

  @GetMapping("/bulk")
  public Response<List<ProductDto>> listProductBulk(
      @RequestParam(name = "ids") List<Integer> ids) throws Exception {
    ListProductBulkQuery query = new ListProductBulkQuery(ids);
    List<ProductDto> products = queryGateway.send(query);
    return Response.success(products);
  }

  @GetMapping("/search")
  public Response<PageDto<ProductDto>> searchProduct(
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
    PageDto<ProductDto> productPage = queryGateway.send(query);
    return Response.success(productPage);
  }

  @PostMapping
  public Response<Integer> createProduct(@Valid @RequestBody CreateProductCommand command)
      throws Exception {
    Integer newProductId = commandGateway.send(command);
    return Response.success(newProductId);
  }

  @PutMapping
  public Response<Void> updateProduct(@Valid @RequestBody UpdateProductCommand command) throws Exception {
    commandGateway.send(command);
    return Response.success();
  }

  @DeleteMapping("/{id}")
  public Response<Void> deleteProduct(@PathVariable("id") Integer id) throws Exception {
    commandGateway.send(new DeleteProductCommand(id));
    return Response.success();
  }

  @PostMapping("/import")
  public Response<Integer> importProduct(@RequestParam("file") MultipartFile file,
      @RequestParam("fileType") FileType fileType) throws Exception {
    byte[] fileContent = file.getBytes();
    ImportProductCommand command = new ImportProductCommand(fileType, fileContent);
    Integer requestId = commandGateway.send(command);
    return Response.success(requestId);
  }

  @GetMapping("/import/{requestId}")
  public Response<ImportProductResultDto> getImportProductResult(@PathVariable("requestId") Integer requestId) throws Exception {
    GetImportProductResultQuery query = new GetImportProductResultQuery(requestId);
    ImportProductResultDto importProductResult = queryGateway.send(query);
    return Response.success(importProductResult);
  }

  @GetMapping("/export")
  public ResponseEntity<byte[]> getImportResult(@RequestParam("fileType") FileType fileType)
      throws Exception {
    ExportProductCommand command = new ExportProductCommand();
    command.setFileType(fileType);
    byte[] bytes = commandGateway.send(command);
    HttpHeaders headers = new HttpHeaders();
    String fileName = "export-product-" +
        DateUtil.format(DateUtil.now(), "yyyyMMddHHmmss") +
        "." +
        command.getFileType().getExtension();
    headers.add("Content-Disposition", "attachment; filename=" + fileName);
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(bytes);
  }
}
