package org.atlas.service.catalog.adapter.persistence.jpa.repository;

import org.atlas.platform.persistence.jpa.core.repository.JpaBaseRepository;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaBrand;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBrandRepository extends JpaBaseRepository<JpaBrand, Integer> {

}