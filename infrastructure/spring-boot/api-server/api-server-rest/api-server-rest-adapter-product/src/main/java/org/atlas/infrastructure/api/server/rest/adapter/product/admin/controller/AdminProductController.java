package org.atlas.infrastructure.api.server.rest.adapter.product.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.shared.enums.ProductStatus;
import org.atlas.domain.product.usecase.admin.AdminCreateProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminCreateProductUseCaseHandler.CreateProductInput;
import org.atlas.domain.product.usecase.admin.AdminCreateProductUseCaseHandler.CreateProductOutput;
import org.atlas.domain.product.usecase.admin.AdminDeleteProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminDeleteProductUseCaseHandler.DeleteProductInput;
import org.atlas.domain.product.usecase.admin.AdminExportProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminExportProductUseCaseHandler.ExportProductInput;
import org.atlas.domain.product.usecase.admin.AdminExportProductUseCaseHandler.ExportProductOutput;
import org.atlas.domain.product.usecase.admin.AdminGetProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminGetProductUseCaseHandler.GetProductInput;
import org.atlas.domain.product.usecase.admin.AdminGetProductUseCaseHandler.GetProductOutput;
import org.atlas.domain.product.usecase.admin.AdminImportProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminImportProductUseCaseHandler.ImportProductInput;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ListProductInput;
import org.atlas.domain.product.usecase.admin.AdminListProductUseCaseHandler.ProductOutput;
import org.atlas.domain.product.usecase.admin.AdminUpdateProductUseCaseHandler;
import org.atlas.domain.product.usecase.admin.AdminUpdateProductUseCaseHandler.UpdateProductInput;
import org.atlas.framework.api.server.rest.response.ApiResponseWrapper;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.file.enums.FileType;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.paging.PagingRequest;
import org.atlas.framework.paging.PagingResult;
import org.atlas.framework.util.DateUtil;
import org.atlas.infrastructure.api.server.rest.adapter.product.admin.model.CreateProductRequest;
import org.atlas.infrastructure.api.server.rest.adapter.product.admin.model.CreateProductResponse;
import org.atlas.infrastructure.api.server.rest.adapter.product.admin.model.DetailedProductResponse;
import org.atlas.infrastructure.api.server.rest.adapter.product.admin.model.ProductResponse;
import org.atlas.infrastructure.api.server.rest.adapter.product.admin.model.UpdateProductRequest;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("api/admin/products")
@Validated
@RequiredArgsConstructor
public class AdminProductController {

  private final AdminListProductUseCaseHandler adminListProductUseCaseHandler;
  private final AdminGetProductUseCaseHandler adminGetProductUseCaseHandler;
  private final AdminCreateProductUseCaseHandler adminCreateProductUseCaseHandler;
  private final AdminUpdateProductUseCaseHandler adminUpdateProductUseCaseHandler;
  private final AdminDeleteProductUseCaseHandler adminDeleteProductUseCaseHandler;
  private final AdminImportProductUseCaseHandler adminImportProductUseCaseHandler;
  private final AdminExportProductUseCaseHandler adminExportProductUseCaseHandler;

  @Operation(summary = "List products with optional filters and pagination.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<List<ProductResponse>> listProduct(
      @Parameter(description = "The unique identifier of the product.", example = "1")
      @RequestParam(name = "id", required = false) Integer id,
      @Parameter(description = "Keyword for searching products.", example = "T-Shirt")
      @RequestParam(name = "keyword", required = false) String keyword,
      @Parameter(description = "Minimum price for filtering products.", example = "10.00")
      @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
      @Parameter(description = "Maximum price for filtering products.", example = "100.00")
      @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
      @Parameter(description = "Status of the product.", example = "IN_STOCK")
      @RequestParam(name = "status", required = false) ProductStatus status,
      @Parameter(description = "Date from which the product is available (ISO 8601 format).", example = "2023-01-01T00:00:00Z")
      @RequestParam(name = "available_from", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date availableFrom,
      @Parameter(description = "Indicates if the product is active.", example = "true")
      @RequestParam(name = "is_active", required = false) Boolean isActive,
      @Parameter(description = "Brand ID for filtering products.", example = "1")
      @RequestParam(name = "brand_id", required = false) Integer brandId,
      @Parameter(description = "List of category IDs for filtering products.", example = "[1, 2, 3]")
      @RequestParam(name = "category_ids", required = false) List<Integer> categoryIds,
      @Parameter(description = "Page number for pagination.", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(description = "Number of items per page.", example = "20")
      @RequestParam(name = "size", required = false, defaultValue = CommonConstant.DEFAULT_PAGE_SIZE_STR) Integer size
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
    PagingResult<ProductOutput> output = adminListProductUseCaseHandler.handle(input);
    PagingResult<ProductResponse> response = ObjectMapperUtil.getInstance()
        .mapPage(output, ProductResponse.class);
    return ApiResponseWrapper.successPage(response);
  }

  @Operation(summary = "Retrieve details of a specific product by ID.")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<DetailedProductResponse> getProduct(
      @Parameter(description = "The unique identifier of the product.", example = "1")
      @PathVariable Integer id) throws Exception {
    GetProductInput input = new GetProductInput(id);
    GetProductOutput output = adminGetProductUseCaseHandler.handle(input);
    DetailedProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, DetailedProductResponse.class);
    return ApiResponseWrapper.success(response);
  }

  @Operation(summary = "Create a new product.")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<CreateProductResponse> createProduct(
      @Parameter(description = "Request object containing the details of the product to create.", required = true)
      @Valid @RequestBody CreateProductRequest request) throws Exception {
    CreateProductInput input = ObjectMapperUtil.getInstance()
        .map(request, CreateProductInput.class);
    CreateProductOutput output = adminCreateProductUseCaseHandler.handle(input);
    CreateProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, CreateProductResponse.class);
    return ApiResponseWrapper.success(response);
  }

  @Operation(summary = "Update an existing product by ID.")
  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<Void> updateProduct(
      @Parameter(description = "The unique identifier of the product to update.", example = "1")
      @PathVariable("id") Integer id,
      @Parameter(description = "Request object containing the new details for the product.", required = true)
      @Valid @RequestBody UpdateProductRequest request) throws Exception {
    UpdateProductInput input = ObjectMapperUtil.getInstance()
        .map(request, UpdateProductInput.class);
    input.setId(id);
    adminUpdateProductUseCaseHandler.handle(input);
    return ApiResponseWrapper.success();
  }

  @Operation(summary = "Delete a product by ID.")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<Void> deleteProduct(
      @Parameter(description = "The unique identifier of the product to delete.", example = "1")
      @PathVariable("id") Integer id) throws Exception {
    DeleteProductInput input = new DeleteProductInput(id);
    adminDeleteProductUseCaseHandler.handle(input);
    return ApiResponseWrapper.success();
  }

  @Operation(summary = "Import products from a file.")
  @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponseWrapper<Void> importProduct(
      @Parameter(description = "The file containing products to import.")
      @RequestParam("file") MultipartFile file,
      @Parameter(description = "The type of the file (e.g., csv, xlsx).", example = "csv")
      @RequestParam("fileType") FileType fileType) throws Exception {
    byte[] fileContent = file.getBytes();
    ImportProductInput input = new ImportProductInput(fileType, fileContent);
    adminImportProductUseCaseHandler.handle(input);
    return ApiResponseWrapper.success();
  }

  @Operation(summary = "Export products based on optional filters.")
  @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> export(
      @Parameter(description = "The unique identifier of the product to export.", example = "1")
      @RequestParam(name = "id", required = false) Integer id,
      @Parameter(description = "Keyword for searching products.", example = "T-Shirt")
      @RequestParam(name = "keyword", required = false) String keyword,
      @Parameter(description = "Minimum price for filtering products.", example = "10.00")
      @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
      @Parameter(description = "Maximum price for filtering products.", example = "100.00")
      @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
      @Parameter(description = "Status of the product.", example = "IN_STOCK")
      @RequestParam(name = "status", required = false) ProductStatus status,
      @Parameter(description = "Date from which the product is available (ISO 8601 format).", example = "2023-01-01T00:00:00Z")
      @RequestParam(name = "available_from", required = false) Date availableFrom,
      @Parameter(description = "Indicates if the product is active.", example = "true")
      @RequestParam(name = "is_active", required = false) Boolean isActive,
      @Parameter(description = "Brand ID for filtering products.", example = "1")
      @RequestParam(name = "brand_id", required = false) Integer brandId,
      @Parameter(description = "List of category IDs for filtering products.", example = "[1, 2, 3]")
      @RequestParam(name = "category_ids", required = false) List<Integer> categoryIds,
      @Parameter(description = "The type of the file to export to (e.g., csv, xlsx).", example = "csv")
      @RequestParam(name = "file_type") FileType fileType
  ) throws Exception {
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

    ExportProductOutput output = adminExportProductUseCaseHandler.handle(input);

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
