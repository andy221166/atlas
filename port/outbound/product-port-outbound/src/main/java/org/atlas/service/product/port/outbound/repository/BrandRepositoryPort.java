package org.atlas.service.product.port.outbound.repository;

import java.util.List;
import org.atlas.domain.product.entity.BrandEntity;

public interface BrandRepositoryPort {

  List<BrandEntity> findAll();
}
