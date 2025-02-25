package org.atlas.service.catalog.adapter.persistence.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.adapter.persistence.jpa.repository.JpaCategoryRepository;
import org.atlas.service.catalog.domain.entity.CategoryEntity;
import org.atlas.service.catalog.port.outbound.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

  private final JpaCategoryRepository jpaCategoryRepository;

  @Override
  public List<CategoryEntity> findAll() {
    return jpaCategoryRepository.findAll()
        .stream()
        .map(jpaCategory -> ObjectMapperUtil.getInstance().map(jpaCategory, CategoryEntity.class))
        .toList();
  }
}
