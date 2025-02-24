package org.atlas.service.product.contract.repository;

import java.util.List;
import org.atlas.service.product.domain.entity.Category;

public interface CategoryRepository {

  List<Category> findAll();
}
