package org.atlas.platform.persistence.mybatis.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.atlas.service.product.domain.Category;

@Mapper
public interface CategoryMapper {

  List<Category> findAll();
}