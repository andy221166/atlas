package org.atlas.platform.persistence.jdbc.product.supports;

import org.atlas.platform.persistence.jdbc.core.NullSafeRowMapper;
import org.atlas.service.product.domain.Category;
import org.atlas.service.product.domain.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        NullSafeRowMapper rowMapper = new NullSafeRowMapper(rs);
        Category category = new Category();
        category.setId(rowMapper.getInt("id"));
        category.setName(rowMapper.getString("name"));
        return category;
    }
}
