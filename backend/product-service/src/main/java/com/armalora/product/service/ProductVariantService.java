package com.armalora.product.service;

import com.armalora.product.dto.ProductVariantRequest;
import com.armalora.product.dto.ProductVariantResponse;
import com.armalora.product.entity.Product;
import com.armalora.product.entity.ProductVariant;
import com.armalora.product.exception.ProductNotFoundException;
import com.armalora.product.repository.ProductRepository;
import com.armalora.product.repository.ProductVariantRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {

    private final ProductVariantRepository
            variantRepository;

    private final ProductRepository
            productRepository;

    public ProductVariantService(
            ProductVariantRepository variantRepository,
            ProductRepository productRepository) {

        this.variantRepository =
                variantRepository;

        this.productRepository =
                productRepository;
    }

    public ProductVariantResponse createVariant(
            Long productId,
            ProductVariantRequest request) {

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        productId
                                )
                        );

        if (variantRepository.existsBySku(
                request.getSku())) {

            throw new IllegalArgumentException(
                    "SKU already exists: "
                            + request.getSku()
            );
        }

        ProductVariant variant =
                new ProductVariant();

        variant.setSku(
                request.getSku()
        );

        variant.setSize(
                request.getSize()
        );

        variant.setColor(
                request.getColor()
        );

        variant.setAdditionalPrice(
                request.getAdditionalPrice()
        );

        variant.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        variant.setProduct(product);

        ProductVariant savedVariant =
                variantRepository.save(variant);

        return convertToResponse(savedVariant);
    }

    public List<ProductVariantResponse>
    getVariantsByProductId(
            Long productId) {

        return variantRepository
                .findByProductIdAndActiveTrue(
                        productId
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ProductVariantResponse
    getVariantById(Long id) {

        ProductVariant variant =
                variantRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Variant not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(variant);
    }

    public void deleteVariant(Long id) {

        if (!variantRepository.existsById(id)) {

            throw new RuntimeException(
                    "Variant not found with id: "
                            + id
            );
        }

        variantRepository.deleteById(id);
    }

    private ProductVariantResponse
    convertToResponse(
            ProductVariant variant) {

        ProductVariantResponse response =
                new ProductVariantResponse();

        response.setId(
                variant.getId()
        );

        response.setSku(
                variant.getSku()
        );

        response.setSize(
                variant.getSize()
        );

        response.setColor(
                variant.getColor()
        );

        response.setAdditionalPrice(
                variant.getAdditionalPrice()
        );

        response.setActive(
                variant.getActive()
        );

        return response;
    }
}