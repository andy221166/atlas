package org.atlas.service.product.port.outbound.repository;

import java.util.List;
import org.atlas.service.product.domain.entity.CategoryEntity;

public interface CategoryRepositoryPort {

  List<CategoryEntity> findAll();
}
