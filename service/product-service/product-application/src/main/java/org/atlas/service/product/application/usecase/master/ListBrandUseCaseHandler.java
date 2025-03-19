package org.atlas.service.product.application.usecase.master;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.domain.entity.BrandEntity;
import org.atlas.service.product.port.inbound.usecase.master.ListBrandUseCase;
import org.atlas.service.product.port.outbound.repository.BrandRepositoryPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListBrandUseCaseHandler implements ListBrandUseCase {

  private final BrandRepositoryPort brandRepositoryPort;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Void input) throws Exception {
    List<BrandEntity> brandEntities = brandRepositoryPort.findAll();
    List<Output.Brand> brands = brandEntities.stream()
        .map(brand -> ObjectMapperUtil.getInstance().map(brand, Output.Brand.class))
        .toList();
    return new Output(brands);
  }
}
