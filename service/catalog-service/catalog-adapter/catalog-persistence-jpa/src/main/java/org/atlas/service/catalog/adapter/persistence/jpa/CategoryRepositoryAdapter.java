package org.atlas.service.catalog.adapter.persistence.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperAdapter;
import org.atlas.service.catalog.adapter.persistence.jpa.repository.JpaCategoryRepository;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.catalog.domain.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

  private final JpaCategoryRepository jpaCategoryRepository;

  @Override
  public List<CategoryEntity> findAll() {
    return jpaCategoryRepository.findAll()
        .stream()
        .map(jpaCategory -> ModelMapperAdapter.map(jpaCategory, CategoryEntity.class))
        .toList();
  }
}
