package org.atlas.service.product.application.master;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.port.inbound.master.ListCategoryUseCase;
import org.atlas.service.product.port.inbound.master.ListCategoryUseCase.ListCategoryOutput.Category;
import org.atlas.service.product.port.outbound.repository.CategoryRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListCategoryUseCaseHandler implements ListCategoryUseCase {

  private final CategoryRepositoryPort categoryRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public ListCategoryOutput handle(Void input) throws Exception {
    List<CategoryEntity> categoryEntities = categoryRepositoryPort.findAll();
    List<Category> categories = ObjectMapperUtil.getInstance()
        .mapList(categoryEntities, Category.class);
    return new ListCategoryOutput(categories);
  }
}
