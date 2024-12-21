package org.atlas.platform.orm.jpa.product.repository;

import org.atlas.platform.orm.jpa.core.repository.JpaBaseRepository;
import org.atlas.platform.orm.jpa.product.entity.JpaCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaBaseRepository<JpaCategory, Integer> {

}