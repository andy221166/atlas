package org.atlas.platform.persistence.mybatis.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.atlas.service.product.domain.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();
}
