package org.atlas.platform.persistence.jpa.product.adapter.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.util.mapping.ModelMapperUtil;
import org.atlas.platform.persistence.jpa.product.entity.JpaProduct;
import org.atlas.platform.persistence.jpa.product.repository.JpaProductRepository;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

  private final JpaProductRepository jpaProductRepository;

  @Override
  public List<Product> findByIdIn(List<Integer> ids) {
    return jpaProductRepository.findAllById(ids)
        .stream()
        .map(jpaProduct -> ModelMapperUtil.map(jpaProduct, Product.class))
        .toList();
  }

  @Override
  public int insertBatch(List<Product> products) {
    List<JpaProduct> jpaProducts = products.stream()
        .map(product -> ModelMapperUtil.map(product, JpaProduct.class))
        .toList();
    List<JpaProduct> savedJpaProducts = jpaProductRepository.saveAll(jpaProducts);
    return savedJpaProducts.size();
  }

  @Override
  public int increaseQuantity(Integer id, Integer amount) {
    return jpaProductRepository.increaseQuantity(id, amount);
  }

  @Override
  public int decreaseQuantity(Integer id, Integer amount) {
    return jpaProductRepository.decreaseQuantity(id, amount);
  }
}
