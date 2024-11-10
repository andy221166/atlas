package org.atlas.platform.persistence.jdbc.product.repository;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.product.supports.CategoryRowMapper;
import org.atlas.service.product.domain.Category;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcCategoryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Category> findAll() {
        String sql = """
            select * 
            from category
            """;
        return namedParameterJdbcTemplate.query(sql, new CategoryRowMapper());
    }
}
