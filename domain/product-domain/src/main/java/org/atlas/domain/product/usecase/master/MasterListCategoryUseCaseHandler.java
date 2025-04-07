package org.atlas.domain.product.usecase.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.repository.CategoryRepository;
import org.atlas.domain.product.usecase.master.MasterListCategoryUseCaseHandler.ListCategoryOutput;
import org.atlas.domain.product.usecase.master.MasterListCategoryUseCaseHandler.ListCategoryOutput.Category;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class MasterListCategoryUseCaseHandler implements UseCaseHandler<Void, ListCategoryOutput> {

  private final CategoryRepository categoryRepository;

  @Override
  public ListCategoryOutput handle(Void input) throws Exception {
    List<CategoryEntity> categoryEntities = categoryRepository.findAll();
    List<Category> categories = ObjectMapperUtil.getInstance()
        .mapList(categoryEntities, Category.class);
    return new ListCategoryOutput(categories);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListCategoryOutput {

    private List<Category> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {

      private Integer id;
      private String name;
    }
  }
}
