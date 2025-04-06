package org.atlas.domain.product.usecase.master;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.repository.BrandRepository;
import org.atlas.domain.product.usecase.master.MasterListBrandUseCaseHandler.ListBrandOutput;
import org.atlas.domain.product.usecase.master.MasterListBrandUseCaseHandler.ListBrandOutput.Brand;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.usecase.UseCaseHandler;

@RequiredArgsConstructor
public class MasterListBrandUseCaseHandler implements UseCaseHandler<Void, ListBrandOutput> {

  private final BrandRepository brandRepository;

  @Override
  public ListBrandOutput handle(Void input) throws Exception {
    List<BrandEntity> brandEntities = brandRepository.findAll();
    List<Brand> brands = ObjectMapperUtil.getInstance()
        .mapList(brandEntities, Brand.class);
    return new ListBrandOutput(brands);
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ListBrandOutput {

    private List<Brand> brands;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Brand {

      private Integer id;
      private String name;
    }
  }
}
