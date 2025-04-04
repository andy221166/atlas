package org.atlas.service.product.adapter.persistence.jpa.repository;

import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaBrandEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBrandRepository extends JpaBaseRepository<JpaBrandEntity, Integer> {

}