package org.atlas.service.catalog.adapter.persistence.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.atlas.service.catalog.domain.entity.CategoryEntity;

@Mapper
public interface CategoryMapper {

  List<CategoryEntity> findAll();
}
