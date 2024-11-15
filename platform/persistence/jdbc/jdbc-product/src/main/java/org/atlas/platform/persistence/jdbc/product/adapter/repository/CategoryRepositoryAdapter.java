package org.atlas.platform.persistence.jdbc.product.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.product.repository.JdbcCategoryRepository;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.domain.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final JdbcCategoryRepository jdbcCategoryRepository;

    @Override
    public List<Category> findAll() {
        return jdbcCategoryRepository.findAll();
    }
}
