package org.atlas.service.catalog.adapter.persistence.jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.adapter.persistence.jpa.repository.JpaBrandRepository;
import org.atlas.service.catalog.domain.entity.BrandEntity;
import org.atlas.service.catalog.port.outbound.repository.BrandRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandRepositoryAdapter implements BrandRepository {

  private final JpaBrandRepository jpaBrandRepository;

  @Override
  public List<BrandEntity> findAll() {
    return jpaBrandRepository.findAll()
        .stream()
        .map(jpaBrand -> ObjectMapperUtil.getInstance().map(jpaBrand, BrandEntity.class))
        .toList();
  }
}
