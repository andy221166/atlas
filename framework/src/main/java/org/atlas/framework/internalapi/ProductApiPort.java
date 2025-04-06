package org.atlas.framework.internalapi;

import org.atlas.domain.product.shared.internal.ListProductInput;
import org.atlas.domain.product.shared.internal.ListProductOutput;

public interface ProductApiPort {

  ListProductOutput call(ListProductInput input);
}
