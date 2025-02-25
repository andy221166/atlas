package org.atlas.service.catalog.port.outbound.repository;

import org.atlas.service.catalog.domain.entity.BrandEntity;

import java.util.List;

public interface BrandRepository {

    List<BrandEntity> findAll();
}
