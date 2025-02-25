package org.atlas.service.catalog.application.usecase.master;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.domain.entity.BrandEntity;
import org.atlas.service.catalog.port.inbound.master.ListBrandUseCase;
import org.atlas.service.catalog.port.outbound.repository.BrandRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListBrandUseCaseHandler implements ListBrandUseCase {

  private final BrandRepository brandRepository;

  @Override
  @Transactional(readOnly = true)
  public Output handle(Void input) throws Exception {
    List<BrandEntity> brandEntities = brandRepository.findAll();
    List<Output.Brand> brands = brandEntities.stream()
        .map(brand -> ObjectMapperUtil.getInstance().map(brand, Output.Brand.class))
        .toList();
    return new Output(brands);
  }
}
