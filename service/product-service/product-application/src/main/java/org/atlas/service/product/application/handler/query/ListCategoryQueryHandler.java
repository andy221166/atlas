package org.atlas.service.product.application.handler.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.service.product.contract.model.CategoryDto;
import org.atlas.service.product.contract.query.ListCategoryQuery;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.domain.Category;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListCategoryQueryHandler implements
    QueryHandler<ListCategoryQuery, List<CategoryDto>> {

  private final CategoryRepository categoryRepository;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryDto> handle(ListCategoryQuery query) throws Exception {
    List<Category> categories = categoryRepository.findAll();
    return categories.stream()
        .map(category -> ModelMapperUtil.map(category, CategoryDto.class))
        .toList();
  }
}
