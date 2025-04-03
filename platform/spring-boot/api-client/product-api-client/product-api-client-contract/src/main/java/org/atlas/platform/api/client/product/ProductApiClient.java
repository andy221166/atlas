package org.atlas.platform.api.client.product;

import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductInput;
import org.atlas.port.inbound.product.internal.ListProductUseCase.ListProductOutput;

public interface ProductApiClient {

  ListProductOutput call(ListProductInput input);
}
