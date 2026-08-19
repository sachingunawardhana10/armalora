package com.armalora.product.controller;

import com.armalora.product.dto.ProductVariantRequest;
import com.armalora.product.dto.ProductVariantResponse;
import com.armalora.product.service.ProductVariantService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/products/{productId}/variants"
)
public class ProductVariantController {

    private final ProductVariantService
            variantService;

    public ProductVariantController(
            ProductVariantService variantService) {

        this.variantService =
                variantService;
    }

    @PostMapping
    public ResponseEntity<ProductVariantResponse>
    createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody
            ProductVariantRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        variantService.createVariant(
                                productId,
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantResponse>>
    getVariants(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                variantService
                        .getVariantsByProductId(
                                productId
                        )
        );
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ProductVariantResponse>
    getVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId) {

        return ResponseEntity.ok(
                variantService
                        .getVariantById(variantId)
        );
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void>
    deleteVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId) {

        variantService.deleteVariant(
                variantId
        );

        return ResponseEntity.noContent()
                .build();
    }
}