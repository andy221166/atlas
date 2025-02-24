package org.atlas.service.catalog.adapter.persistence.mybatis.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.catalog.domain.entity.ProductEntity;

@Mapper
public interface ProductMapper {

  List<ProductEntity> findByIdIn(@Param("ids") List<Integer> ids);

  List<ProductEntity> find(@Param("query") SearchProductQuery query);

  long count(@Param("query") SearchProductQuery query);

  int insertBatch(@Param("products") List<ProductEntity> productEntities);

  int decreaseQuantity(@Param("id") Integer id, @Param("amount") Integer amount);
}
