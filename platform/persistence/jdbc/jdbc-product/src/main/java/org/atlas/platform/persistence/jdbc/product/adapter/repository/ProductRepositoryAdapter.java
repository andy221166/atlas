package org.atlas.platform.persistence.jdbc.product.adapter.repository;

import lombok.RequiredArgsConstructor;
import org.atlas.platform.persistence.jdbc.product.repository.JdbcProductRepository;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JdbcProductRepository jdbcProductRepository;

    @Override
    public List<Product> findByIdIn(List<Integer> ids) {
        return jdbcProductRepository.findByIdIn(ids);
    }

    @Override
    public int insertBatch(List<Product> products) {
        return jdbcProductRepository.insertBatch(products);
    }

    @Override
    public int increaseQuantity(Integer id, Integer amount) {
        return jdbcProductRepository.increaseQuantity(id, amount);
    }

    @Override
    public int decreaseQuantity(Integer id, Integer amount) {
        return jdbcProductRepository.decreaseQuantity(id, amount);
    }
}
