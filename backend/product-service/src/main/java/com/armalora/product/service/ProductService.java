package com.armalora.product.service;

import com.armalora.product.dto.ProductRequest;
import com.armalora.product.dto.ProductResponse;
import com.armalora.product.entity.Category;
import com.armalora.product.entity.Product;
import com.armalora.product.exception.ProductNotFoundException;
import com.armalora.product.repository.CategoryRepository;
import com.armalora.product.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        )
                );

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setImageUrl(request.getImageUrl());

        product.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        Product savedProduct =
                productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    public Page<ProductResponse> getAllProducts(
            Pageable pageable) {

        return productRepository
                .findByActiveTrue(pageable)
                .map(this::convertToResponse);
    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );

        return convertToResponse(product);
    }

    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found with id: "
                                        + request.getCategoryId()
                        )
                );

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(
                request.getStockQuantity()
        );

        existingProduct.setCategory(category);

        existingProduct.setImageUrl(
                request.getImageUrl()
        );

        if (request.getActive() != null) {
            existingProduct.setActive(
                    request.getActive()
            );
        }

        Product updatedProduct =
                productRepository.save(existingProduct);

        return convertToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {

            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }

    public List<ProductResponse> searchProducts(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        List<Product> products;

        if (name != null && !name.isBlank()) {

            products =
                    productRepository
                            .findByActiveTrueAndNameContainingIgnoreCase(
                                    name
                            );

        } else if (categoryId != null) {

            products =
                    productRepository
                            .findByActiveTrueAndCategoryId(
                                    categoryId
                            );

        } else if (minPrice != null && maxPrice != null) {

            products =
                    productRepository
                            .findByActiveTrueAndPriceBetween(
                                    minPrice,
                                    maxPrice
                            );

        } else {

            products =
                    productRepository.findByActiveTrue();
        }

        return products.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ProductResponse convertToResponse(
            Product product) {

        ProductResponse response =
                new ProductResponse();

        response.setId(product.getId());

        response.setName(
                product.getName()
        );

        response.setDescription(
                product.getDescription()
        );

        response.setPrice(
                product.getPrice()
        );

        response.setStockQuantity(
                product.getStockQuantity()
        );

        // Category information
        if (product.getCategory() != null) {

            response.setCategoryId(
                    product.getCategory().getId()
            );

            response.setCategoryName(
                    product.getCategory().getName()
            );
        }

        response.setImageUrl(
                product.getImageUrl()
        );

        response.setActive(
                product.getActive()
        );

        response.setCreatedAt(
                product.getCreatedAt()
        );

        response.setUpdatedAt(
                product.getUpdatedAt()
        );

        return response;
    }
}