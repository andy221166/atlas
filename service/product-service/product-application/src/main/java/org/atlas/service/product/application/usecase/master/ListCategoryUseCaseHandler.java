package org.atlas.service.product.application.usecase.master;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.port.inbound.usecase.master.ListCategoryUseCase;
import org.atlas.service.product.port.outbound.repository.CategoryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListCategoryUseCaseHandler implements ListCategoryUseCase {

  private final CategoryRepository categoryRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Void input) throws Exception {
    List<CategoryEntity> categoryEntities = categoryRepository.findAll();
    List<Output.Category> categories = categoryEntities.stream()
        .map(brand -> ObjectMapperUtil.getInstance().map(brand, Output.Category.class))
        .toList();
    return new Output(categories);
  }
}
