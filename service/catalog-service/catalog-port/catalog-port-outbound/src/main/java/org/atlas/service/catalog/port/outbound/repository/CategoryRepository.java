package org.atlas.service.catalog.port.outbound.repository;

import org.atlas.service.catalog.domain.entity.CategoryEntity;

import java.util.List;

public interface CategoryRepository {

    List<CategoryEntity> findAll();
}
