package org.atlas.infrastructure.api.server.rest.adapter.product.internal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductInput;
import org.atlas.domain.product.usecase.internal.InternalListProductUseCaseHandler.ListProductOutput;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.infrastructure.api.server.rest.adapter.product.internal.model.ListProductRequest;
import org.atlas.infrastructure.api.server.rest.adapter.product.internal.model.ListProductResponse;
import org.atlas.infrastructure.api.server.rest.core.response.Response;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/internal/products")
@Validated
@RequiredArgsConstructor
public class InternalProductController {

  private final InternalListProductUseCaseHandler internalListProductUseCaseHandler;

  @Operation(summary = "List Products", description = "Retrieves a list of products based on the provided criteria.")
  @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<ListProductResponse> listProduct(
      @Parameter(description = "Request object containing the criteria for listing products.", required = true)
      @Valid @RequestBody ListProductRequest request) throws Exception {
    ListProductInput input = ObjectMapperUtil.getInstance()
        .map(request, ListProductInput.class);
    ListProductOutput output = internalListProductUseCaseHandler.handle(input);
    ListProductResponse response = ObjectMapperUtil.getInstance()
        .map(output, ListProductResponse.class);
    return Response.success(response);
  }
}
