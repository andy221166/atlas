package org.atlas.service.product.application.master;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.BrandEntity;
import org.atlas.service.product.port.inbound.master.ListBrandUseCase;
import org.atlas.service.product.port.inbound.master.ListBrandUseCase.ListBrandOutput.Brand;
import org.atlas.service.product.port.outbound.repository.BrandRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListBrandUseCaseHandler implements ListBrandUseCase {

  private final BrandRepositoryPort brandRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public ListBrandOutput handle(Void input) throws Exception {
    List<BrandEntity> brandEntities = brandRepositoryPort.findAll();
    List<Brand> brands = ObjectMapperUtil.getInstance()
        .mapList(brandEntities, Brand.class);
    return new ListBrandOutput(brands);
  }
}
