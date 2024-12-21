package org.atlas.platform.orm.jpa.product.adapter.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.orm.jpa.product.repository.JpaCategoryRepository;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.domain.Category;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

  private final JpaCategoryRepository jpaCategoryRepository;

  @Override
  public List<Category> findAll() {
    return jpaCategoryRepository.findAll()
        .stream()
        .map(jpaCategory -> ModelMapperUtil.map(jpaCategory, Category.class))
        .toList();
  }
}
