package org.atlas.domain.product.usecase.common.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.repository.CategoryRepository;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class ListCategoryUseCaseHandler implements UseCaseHandler<Void, List<CategoryEntity>> {

  private final CategoryRepository categoryRepository;

  @Override
  public List<CategoryEntity> handle(Void input) throws Exception {
    return categoryRepository.findAll();
  }
}
