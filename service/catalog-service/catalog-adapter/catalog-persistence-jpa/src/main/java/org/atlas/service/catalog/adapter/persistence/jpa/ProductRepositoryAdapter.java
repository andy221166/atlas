package org.atlas.service.catalog.adapter.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.commons.paging.PagingRequest;
import org.atlas.platform.commons.paging.PagingResult;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.catalog.adapter.persistence.jpa.entity.JpaProduct;
import org.atlas.service.catalog.adapter.persistence.jpa.repository.JpaProductRepository;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.atlas.service.catalog.port.outbound.repository.FindProductCriteria;
import org.atlas.service.catalog.port.outbound.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

  private final JpaProductRepository jpaProductRepository;

  @Override
  public List<ProductEntity> findByIdIn(List<Integer> ids) {
    return jpaProductRepository.findAllById(ids)
        .stream()
        .map(jpaProduct ->
            ObjectMapperUtil.getInstance().map(jpaProduct, ProductEntity.class))
        .toList();
  }

  @Override
  public PagingResult<ProductEntity> findByCriteria(FindProductCriteria criteria, PagingRequest pagingRequest) {
    long totalCount = jpaProductRepository.count(criteria);
    if (totalCount == 0L) {
      return PagingResult.empty();
    }
    List<JpaProduct> jpaProducts = jpaProductRepository.find(criteria, pagingRequest);
    List<ProductEntity> productEntities = ObjectMapperUtil.getInstance().mapList(jpaProducts, ProductEntity.class);
    return PagingResult.of(productEntities, totalCount);
  }

  @Override
  public Optional<ProductEntity> findById(Integer id) {
    return jpaProductRepository.findById(id)
            .map(jpaProduct -> ObjectMapperUtil.getInstance().map(jpaProduct, ProductEntity.class));
  }

  @Override
  public int insert(ProductEntity productEntity) {
    JpaProduct jpaProduct = ObjectMapperUtil.getInstance().map(productEntity, JpaProduct.class);
    jpaProductRepository.save(jpaProduct);
    productEntity.setId(jpaProduct.getId());
    return 1;
  }

  @Override
  public int insertBatch(List<ProductEntity> productEntities) {
    List<JpaProduct> jpaProducts = ObjectMapperUtil.getInstance().mapList(productEntities, JpaProduct.class);
    jpaProductRepository.saveAll(jpaProducts);
    return jpaProducts.size();
  }

  @Override
  public int update(ProductEntity productEntity) {
    JpaProduct jpaProduct = ObjectMapperUtil.getInstance().map(productEntity, JpaProduct.class);
    jpaProductRepository.save(jpaProduct);
    return 1;
  }

  @Override
  public int updateBatch(List<ProductEntity> productEntities) {
    List<JpaProduct> jpaProducts = ObjectMapperUtil.getInstance().mapList(productEntities, JpaProduct.class);
    jpaProductRepository.saveAll(jpaProducts);
    return jpaProducts.size();
  }

  @Override
  public int delete(ProductEntity productEntity) {
    JpaProduct jpaProduct = ObjectMapperUtil.getInstance().map(productEntity, JpaProduct.class);
    jpaProductRepository.delete(jpaProduct);
    return 1;
  }

  @Override
  public int deleteBatch(List<ProductEntity> productEntities) {
    List<JpaProduct> jpaProducts = ObjectMapperUtil.getInstance().mapList(productEntities, JpaProduct.class);
    jpaProductRepository.deleteAll(jpaProducts);
    return jpaProducts.size();
  }
}
