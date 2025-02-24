package org.atlas.service.catalog.application.handler.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.cqrs.query.QueryHandler;
import org.atlas.service.product.contract.model.CategoryDto;
import org.atlas.service.product.contract.query.ListCategoryQuery;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.catalog.domain.entity.CategoryEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListCategoryQueryHandler implements
    QueryHandler<ListCategoryQuery, List<CategoryDto>> {

  private final CategoryRepository categoryRepository;

  @Override
  @Cacheable("categories")
  @Transactional(readOnly = true)
  public List<CategoryDto> handle(ListCategoryQuery query) throws Exception {
    List<CategoryEntity> categories = categoryRepository.findAll();
    return categories.stream()
        .map(category -> ModelMapperUtil.map(category, CategoryDto.class))
        .toList();
  }
}
