package org.atlas.platform.persistence.mybatis.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.atlas.service.product.contract.query.SearchProductQuery;
import org.atlas.service.product.domain.Product;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findByIdIn(@Param("ids") List<Integer> ids);
    List<Product> find(@Param("command") SearchProductQuery command);
    long count(@Param("command") SearchProductQuery command);
    int insertBatch(@Param("products") List<Product> products);
    int increaseQuantity(@Param("id") Integer id, @Param("amount") Integer amount);
    int decreaseQuantity(@Param("id") Integer id, @Param("amount") Integer amount);
}
