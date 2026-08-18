package com.armalora.product.service;

import com.armalora.product.dto.ProductRequest;
import com.armalora.product.dto.ProductResponse;
import com.armalora.product.entity.Product;
import com.armalora.product.exception.ProductNotFoundException;
import com.armalora.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // =========================
    // CREATE PRODUCT
    // =========================

    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        // If active is not provided, default to true
        product.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    // =========================
    // GET ALL ACTIVE PRODUCTS
    // =========================

    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================
    // GET PRODUCT BY ID
    // =========================

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );

        return convertToResponse(product);
    }

    // =========================
    // UPDATE PRODUCT
    // =========================

    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(id)
                );

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setImageUrl(request.getImageUrl());

        // Only update active if a value was provided
        if (request.getActive() != null) {
            existingProduct.setActive(request.getActive());
        }

        Product updatedProduct =
                productRepository.save(existingProduct);

        return convertToResponse(updatedProduct);
    }

    // =========================
    // DELETE PRODUCT
    // =========================

    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {

            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }

    // =========================
    // ENTITY → RESPONSE DTO
    // =========================

    private ProductResponse convertToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
}