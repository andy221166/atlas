package org.atlas.service.product.adapter.api.server.rest.admin.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.constant.Constant;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.util.DateUtil;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.usecase.admin.CreateProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.DeleteProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.ExportProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.ImportProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.ListProductUseCase;
import org.atlas.service.product.port.inbound.usecase.admin.UpdateProductUseCase;
import org.atlas.service.product.adapter.api.server.rest.admin.model.CreateProductRequest;
import org.atlas.service.product.adapter.api.server.rest.admin.model.UpdateProductRequest;
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

@RestController("adminProductController")
@RequestMapping("api/admin/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

  private final ListProductUseCase listProductUseCase;
  private final CreateProductUseCase createProductUseCase;
  private final UpdateProductUseCase updateProductUseCase;
  private final DeleteProductUseCase deleteProductUseCase;
  private final ImportProductUseCase importProductUseCase;
  private final ExportProductUseCase exportProductUseCase;

  @GetMapping
  public Response<ListProductUseCase.Output> listProduct(
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
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer size
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
    input.setPagingRequest(PagingRequest.of(page - 1, size));
    ListProductUseCase.Output output = listProductUseCase.handle(input);
    return Response.success(output);
  }

  @PostMapping
  public Response<CreateProductUseCase.Output> createProduct(
      @Valid @RequestBody CreateProductRequest request) throws Exception {
    CreateProductUseCase.Input input = ObjectMapperUtil.getInstance()
        .map(request, CreateProductUseCase.Input.class);
    CreateProductUseCase.Output output = createProductUseCase.handle(input);
    return Response.success(output);
  }

  @PutMapping("/{id}")
  public Response<Void> updateProduct(
      @PathVariable("id") Integer id,
      @Valid @RequestBody UpdateProductRequest request) throws Exception {
    UpdateProductUseCase.Input input = ObjectMapperUtil.getInstance()
        .map(request, UpdateProductUseCase.Input.class);
    input.setId(id);
    updateProductUseCase.handle(input);
    return Response.success();
  }

  @DeleteMapping("/{id}")
  public Response<Void> deleteProduct(@PathVariable("id") Integer id) throws Exception {
    DeleteProductUseCase.Input input = new DeleteProductUseCase.Input(id);
    deleteProductUseCase.handle(input);
    return Response.success();
  }

  @PostMapping("/import")
  public Response<Integer> importProduct(@RequestParam("file") MultipartFile file,
      @RequestParam("fileType") FileType fileType) throws Exception {
    byte[] fileContent = file.getBytes();
    ImportProductUseCase.Input input = new ImportProductUseCase.Input(fileType, fileContent);
    importProductUseCase.handle(input);
    return Response.success();
  }

  @GetMapping("/export")
  public ResponseEntity<byte[]> export(
      @RequestParam(name = "id", required = false) Integer id,
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
      @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
      @RequestParam(name = "status", required = false) ProductStatus status,
      @RequestParam(name = "available_from", required = false) Date availableFrom,
      @RequestParam(name = "is_active", required = false) Boolean isActive,
      @RequestParam(name = "brand_id", required = false) Integer brandId,
      @RequestParam(name = "category_ids", required = false) List<Integer> categoryIds,
      @RequestParam(name = "file_type") FileType fileType)
      throws Exception {
    ExportProductUseCase.Input input = new ExportProductUseCase.Input();
    input.setId(id);
    input.setKeyword(keyword);
    input.setMinPrice(minPrice);
    input.setMaxPrice(maxPrice);
    input.setStatus(status);
    input.setAvailableFrom(availableFrom);
    input.setIsActive(isActive);
    input.setBrandId(brandId);
    input.setCategoryIds(categoryIds);

    ExportProductUseCase.Output output = exportProductUseCase.handle(input);

    HttpHeaders headers = new HttpHeaders();
    String fileName = "export-product-" +
        DateUtil.now("yyyyMMddHHmmss") +
        "." +
        fileType.getExtension();
    headers.add("Content-Disposition", "attachment; filename=" + fileName);
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(output.getFileContent());
  }
}
