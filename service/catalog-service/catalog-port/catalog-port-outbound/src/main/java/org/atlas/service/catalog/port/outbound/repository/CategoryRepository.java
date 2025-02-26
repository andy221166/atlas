package org.atlas.service.catalog.port.outbound.repository;

import java.util.List;
import org.atlas.service.catalog.domain.entity.CategoryEntity;

public interface CategoryRepository {

    List<CategoryEntity> findAll();
}
