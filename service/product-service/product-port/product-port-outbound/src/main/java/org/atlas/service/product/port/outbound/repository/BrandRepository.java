package org.atlas.service.product.port.outbound.repository;

import java.util.List;
import org.atlas.service.product.domain.entity.BrandEntity;

public interface BrandRepository {

  List<BrandEntity> findAll();
}
