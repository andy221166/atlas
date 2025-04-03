package org.atlas.service.product.adapter.persistence.jpa.repository;

import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.product.adapter.persistence.jpa.entity.JpaCategoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaBaseRepository<JpaCategoryEntity, Integer> {

}