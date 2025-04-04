package org.atlas.service.product.adapter.api.server.rest.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.atlas.domain.product.entity.ProductStatus;
import org.atlas.port.inbound.product.admin.CreateProductUseCase;
import org.atlas.port.inbound.product.admin.CreateProductUseCase.CreateProductInput;
import org.atlas.port.inbound.product.admin.CreateProductUseCase.CreateProductOutput;
import org.atlas.port.inbound.product.admin.DeleteProductUseCase;
import org.atlas.port.inbound.product.admin.DeleteProductUseCase.DeleteProductInput;
import org.atlas.port.inbound.product.admin.ExportProductUseCase;
import org.atlas.port.inbound.product.admin.ExportProductUseCase.ExportProductInput;
import org.atlas.port.inbound.product.admin.ExportProductUseCase.ExportProductOutput;
import org.atlas.port.inbound.product.admin.GetProductUseCase;
import org.atlas.port.inbound.product.admin.GetProductUseCase.GetProductInput;
import org.atlas.port.inbound.product.admin.GetProductUseCase.GetProductOutput;
import org.atlas.port.inbound.product.admin.ImportProductUseCase;
import org.atlas.port.inbound.product.admin.ImportProductUseCase.ImportProductInput;
import org.atlas.port.inbound.product.admin.ListProductUseCase;
import org.atlas.port.inbound.product.admin.ListProductUseCase.ListProductInput;
import org.atlas.port.inbound.product.admin.ListProductUseCase.ListProductOutput;
import org.atlas.port.inbound.product.admin.UpdateProductUseCase;
import org.atlas.port.inbound.product.admin.UpdateProductUseCase.UpdateProductInput;
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

  @Operation(summary = "List products with optional filters and pagination.")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListProductResponse> listProduct(
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
      @RequestParam(name = "available_from", required = false) Date availableFrom,
      @Parameter(description = "Indicates if the product is active.", example = "true")
      @RequestParam(name = "is_active", required = false) Boolean isActive,
      @Parameter(description = "Brand ID for filtering products.", example = "1")
      @RequestParam(name = "brand_id", required = false) Integer brandId,
      @Parameter(description = "List of category IDs for filtering products.", example = "[1, 2, 3]")
      @RequestParam(name = "category_ids", required = false) List<Integer> categoryIds,
      @Parameter(description = "Page number for pagination.", example = "1")
      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
      @Parameter(description = "Number of items per page.", example = "20")
      @RequestParam(name = "size", required = false, defaultValue = Constant.DEFAULT_PAGE_SIZE_STR) Integer size
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

  @Operation(summary = "Retrieve details of a specific product by ID.")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<GetProductResponse> getProduct(
      @Parameter(description = "The unique identifier of the product.", example = "1")
      @PathVariable Integer id) throws Exception {
    GetProductInput input = new GetProductInput(id);
    GetProductOutput output = getProductUseCase.handle(input);
    GetProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, GetProductResponse.class);
    return Response.success(response);
  }

  @Operation(summary = "Create a new product.")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<CreateProductResponse> createProduct(
      @Parameter(description = "Request object containing the details of the product to create.", required = true)
      @Valid @RequestBody CreateProductRequest request) throws Exception {
    CreateProductInput input = ObjectMapperUtil.getInstance()
        .map(request, CreateProductInput.class);
    CreateProductOutput output = createProductUseCase.handle(input);
    CreateProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, CreateProductResponse.class);
    return Response.success(response);
  }

  @Operation(summary = "Update an existing product by ID.")
  @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> updateProduct(
      @Parameter(description = "The unique identifier of the product to update.", example = "1")
      @PathVariable("id") Integer id,
      @Parameter(description = "Request object containing the new details for the product.", required = true)
      @Valid @RequestBody UpdateProductRequest request) throws Exception {
    UpdateProductInput input = ObjectMapperUtil.getInstance()
        .map(request, UpdateProductInput.class);
    input.setId(id);
    updateProductUseCase.handle(input);
    return Response.success();
  }

  @Operation(summary = "Delete a product by ID.")
  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> deleteProduct(
      @Parameter(description = "The unique identifier of the product to delete.", example = "1")
      @PathVariable("id") Integer id) throws Exception {
    DeleteProductInput input = new DeleteProductInput(id);
    deleteProductUseCase.handle(input);
    return Response.success();
  }

  @Operation(summary = "Import products from a file.")
  @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> importProduct(
      @Parameter(description = "The file containing products to import.")
      @RequestParam("file") MultipartFile file,
      @Parameter(description = "The type of the file (e.g., csv, xlsx).", example = "csv")
      @RequestParam("fileType") FileType fileType) throws Exception {
    byte[] fileContent = file.getBytes();
    ImportProductInput input = new ImportProductInput(fileType, fileContent);
    importProductUseCase.handle(input);
    return Response.success();
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
