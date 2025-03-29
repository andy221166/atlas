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
import org.atlas.service.product.adapter.api.server.rest.admin.model.CreateProductRequest;
import org.atlas.service.product.adapter.api.server.rest.admin.model.CreateProductResponse;
import org.atlas.service.product.adapter.api.server.rest.admin.model.GetProductResponse;
import org.atlas.service.product.adapter.api.server.rest.admin.model.ListProductResponse;
import org.atlas.service.product.adapter.api.server.rest.admin.model.UpdateProductRequest;
import org.atlas.service.product.domain.entity.ProductStatus;
import org.atlas.service.product.port.inbound.admin.CreateProductUseCase;
import org.atlas.service.product.port.inbound.admin.CreateProductUseCase.CreateProductInput;
import org.atlas.service.product.port.inbound.admin.CreateProductUseCase.CreateProductOutput;
import org.atlas.service.product.port.inbound.admin.DeleteProductUseCase;
import org.atlas.service.product.port.inbound.admin.DeleteProductUseCase.DeleteProductInput;
import org.atlas.service.product.port.inbound.admin.ExportProductUseCase;
import org.atlas.service.product.port.inbound.admin.ExportProductUseCase.ExportProductInput;
import org.atlas.service.product.port.inbound.admin.ExportProductUseCase.ExportProductOutput;
import org.atlas.service.product.port.inbound.admin.GetProductUseCase;
import org.atlas.service.product.port.inbound.admin.GetProductUseCase.GetProductInput;
import org.atlas.service.product.port.inbound.admin.GetProductUseCase.GetProductOutput;
import org.atlas.service.product.port.inbound.admin.ImportProductUseCase;
import org.atlas.service.product.port.inbound.admin.ImportProductUseCase.ImportProductInput;
import org.atlas.service.product.port.inbound.admin.ListProductUseCase;
import org.atlas.service.product.port.inbound.admin.ListProductUseCase.ListProductInput;
import org.atlas.service.product.port.inbound.admin.ListProductUseCase.ListProductOutput;
import org.atlas.service.product.port.inbound.admin.UpdateProductUseCase;
import org.atlas.service.product.port.inbound.admin.UpdateProductUseCase.UpdateProductInput;
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
  private final GetProductUseCase getProductUseCase;
  private final CreateProductUseCase createProductUseCase;
  private final UpdateProductUseCase updateProductUseCase;
  private final DeleteProductUseCase deleteProductUseCase;
  private final ImportProductUseCase importProductUseCase;
  private final ExportProductUseCase exportProductUseCase;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListProductResponse> listProduct(
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
    ListProductInput input = new ListProductInput();
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
    ListProductOutput output = listProductUseCase.handle(input);
    ListProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListProductResponse.class);
    return Response.success(response);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<GetProductResponse> getProduct(@PathVariable Integer id) throws Exception {
    GetProductInput input = new GetProductInput(id);
    GetProductOutput output = getProductUseCase.handle(input);
    GetProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProductResponse.class);
    return Response.success(response);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<CreateProductResponse> createProduct(
      @Valid @RequestBody CreateProductRequest request) throws Exception {
    CreateProductInput input = ObjectMapperUtil.getInstance()
        .map(request, CreateProductInput.class);
    CreateProductOutput output = createProductUseCase.handle(input);
    CreateProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, CreateProductResponse.class);
    return Response.success(response);
  }

  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> updateProduct(
      @PathVariable("id") Integer id,
      @Valid @RequestBody UpdateProductRequest request) throws Exception {
    UpdateProductInput input = ObjectMapperUtil.getInstance()
        .map(request, UpdateProductInput.class);
    input.setId(id);
    updateProductUseCase.handle(input);
    return Response.success();
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> deleteProduct(@PathVariable("id") Integer id) throws Exception {
    DeleteProductInput input = new DeleteProductInput(id);
    deleteProductUseCase.handle(input);
    return Response.success();
  }

  @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> importProduct(@RequestParam("file") MultipartFile file,
      @RequestParam("fileType") FileType fileType) throws Exception {
    byte[] fileContent = file.getBytes();
    ImportProductInput input = new ImportProductInput(fileType, fileContent);
    importProductUseCase.handle(input);
    return Response.success();
  }

  @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
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
    ExportProductInput input = new ExportProductInput();
    input.setId(id);
    input.setKeyword(keyword);
    input.setMinPrice(minPrice);
    input.setMaxPrice(maxPrice);
    input.setStatus(status);
    input.setAvailableFrom(availableFrom);
    input.setIsActive(isActive);
    input.setBrandId(brandId);
    input.setCategoryIds(categoryIds);

    ExportProductOutput output = exportProductUseCase.handle(input);

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
