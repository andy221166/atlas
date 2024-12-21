package org.atlas.platform.orm.mybatis.product.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.domain.Product;

@Mapper
public interface ProductMapper {

  List<Product> findByIdIn(@Param("ids") List<Integer> ids);

  List<Product> find(@Param("query") SearchProductQuery query);

  long count(@Param("query") SearchProductQuery query);

  int insertBatch(@Param("products") List<Product> products);

  int increaseQuantity(@Param("id") Integer id, @Param("amount") Integer amount);

  int decreaseQuantity(@Param("id") Integer id, @Param("amount") Integer amount);
}
