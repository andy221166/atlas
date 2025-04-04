package org.atlas.service.product.adapter.persistence.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.adapter.persistence.jpa.repository.JpaCategoryRepository;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.service.product.port.outbound.repository.CategoryRepositoryPort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

  private final JpaCategoryRepository jpaCategoryRepository;

  @Override
  public List<CategoryEntity> findAll() {
    return jpaCategoryRepository.findAll()
        .stream()
        .map(jpaCategory -> ObjectMapperUtil.getInstance().map(jpaCategory, CategoryEntity.class))
        .toList();
  }
}
