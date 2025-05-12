package org.atlas.domain.product.usecase.common.handler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.repository.BrandRepository;
import org.atlas.framework.usecase.handler.UseCaseHandler;

@RequiredArgsConstructor
public class ListBrandUseCaseHandler implements UseCaseHandler<Void, List<BrandEntity>> {

  private final BrandRepository brandRepository;

  @Override
  public List<BrandEntity> handle(Void input) throws Exception {
    return brandRepository.findAll();
  }
}
