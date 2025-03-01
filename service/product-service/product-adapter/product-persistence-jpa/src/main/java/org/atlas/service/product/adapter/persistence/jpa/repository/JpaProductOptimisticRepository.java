package org.atlas.service.product.adapter.persistence.jpa.repository;

import org.atlas.service.product.adapter.persistence.jpa.entity.JpaProductEntityOptimisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductOptimisticRepository extends
    JpaRepository<JpaProductEntityOptimisticEntity, Integer> {
}
