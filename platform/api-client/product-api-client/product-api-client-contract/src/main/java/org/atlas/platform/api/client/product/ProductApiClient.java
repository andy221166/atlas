package org.atlas.platform.api.client.product;

import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductInput;
import org.atlas.service.product.port.inbound.internal.ListProductUseCase.ListProductOutput;

public interface ProductApiClient {

  ListProductOutput call(ListProductInput input);
}
