package org.atlas.service.product.contract.repository;

import org.atlas.service.product.domain.Category;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll();
}
