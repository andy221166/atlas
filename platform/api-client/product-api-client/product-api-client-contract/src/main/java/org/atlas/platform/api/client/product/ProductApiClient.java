package org.atlas.platform.api.client.product;

import org.atlas.service.product.port.inbound.usecase.internal.ListProductUseCase;

public interface ProductApiClient {

  ListProductUseCase.Output call(ListProductUseCase.Input input);
}
