package org.atlas.service.user.application.handler.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.model.CategoryDto;
import org.atlas.service.product.contract.query.ListCategoryQuery;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.domain.Category;
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
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
        .map(category -> ObjectMapperUtil.getInstance().map(category, CategoryDto.class))
        .toList();
  }
}
