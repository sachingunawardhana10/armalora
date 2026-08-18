package com.armalora.product.repository;

import com.armalora.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    List<Product> findByActiveTrue();

    Page<Product> findByActiveTrue(Pageable pageable);

    List<Product> findByActiveTrueAndNameContainingIgnoreCase(
            String name
    );

    List<Product> findByActiveTrueAndCategoryId(
            Long categoryId
    );

    List<Product> findByActiveTrueAndPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );
}