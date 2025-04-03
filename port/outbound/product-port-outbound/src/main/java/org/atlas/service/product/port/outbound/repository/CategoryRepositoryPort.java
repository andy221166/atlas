package org.atlas.service.product.port.outbound.repository;

import java.util.List;
import org.atlas.domain.product.entity.CategoryEntity;

public interface CategoryRepositoryPort {

  List<CategoryEntity> findAll();
}
