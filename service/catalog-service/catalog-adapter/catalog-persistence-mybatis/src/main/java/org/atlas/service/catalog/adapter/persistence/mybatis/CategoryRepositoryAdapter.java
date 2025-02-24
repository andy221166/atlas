package org.atlas.service.catalog.adapter.persistence.mybatis;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.service.catalog.adapter.persistence.mybatis.mapper.CategoryMapper;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.catalog.domain.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

  private final CategoryMapper categoryMapper;

  @Override
  public List<CategoryEntity> findAll() {
    return categoryMapper.findAll();
  }
}
