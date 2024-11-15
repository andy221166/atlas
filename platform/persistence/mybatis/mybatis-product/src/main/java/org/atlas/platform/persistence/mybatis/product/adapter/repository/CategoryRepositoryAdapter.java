package org.atlas.platform.persistence.mybatis.product.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.mybatis.product.mapper.CategoryMapper;
import org.atlas.service.product.contract.repository.CategoryRepository;
import org.atlas.service.product.domain.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
}
