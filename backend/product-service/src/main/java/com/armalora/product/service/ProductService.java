package com.armalora.product.service;

import com.armalora.product.dto.ProductImageResponse;
import com.armalora.product.dto.ProductRequest;
import com.armalora.product.dto.ProductResponse;
import com.armalora.product.dto.ProductVariantResponse;
import com.armalora.product.entity.Category;
import com.armalora.product.entity.Product;
import com.armalora.product.entity.ProductImage;
import com.armalora.product.entity.ProductVariant;
import com.armalora.product.exception.CategoryNotFoundException;
import com.armalora.product.exception.ProductNotFoundException;
import com.armalora.product.repository.CategoryRepository;
import com.armalora.product.repository.ProductRepository;
import com.armalora.product.specification.ProductSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

        // Find category
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                request.getCategoryId()
                        )
                );

        // Create Product
        Product product = new Product();

        product.setName(
                request.getName().trim()
        );

        product.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        product.setPrice(
                request.getPrice()
        );

        product.setCategory(category);

        product.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        // Save product
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

        // Find existing product
        Product existingProduct =
                productRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(id)
                        );

        // Find category
        Category category =
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new CategoryNotFoundException(
                                        request.getCategoryId()
                                )
                        );

        // Update product
        existingProduct.setName(
                request.getName().trim()
        );

        existingProduct.setDescription(
                request.getDescription() == null
                        ? null
                        : request.getDescription().trim()
        );

        existingProduct.setPrice(
                request.getPrice()
        );

        existingProduct.setCategory(category);

        if (request.getActive() != null) {

            existingProduct.setActive(
                    request.getActive()
            );
        }

        // Save
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

    public Page<ProductResponse> searchProducts(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        // Validate page
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page cannot be negative"
            );
        }

        // Validate page size
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        // Validate price range
        if (minPrice != null
                && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "Minimum price cannot be greater than maximum price"
            );
        }

        // Default sort field
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        // Default direction
        if (direction == null || direction.isBlank()) {
            direction = "asc";
        }

        Sort sort;

        if ("desc".equalsIgnoreCase(direction)) {

            sort = Sort.by(sortBy).descending();

        } else if ("asc".equalsIgnoreCase(direction)) {

            sort = Sort.by(sortBy).ascending();

        } else {

            throw new IllegalArgumentException(
                    "Direction must be 'asc' or 'desc'"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Specification<Product> specification =
                Specification.where((Specification<Product>) null);

        // Name filter
        if (name != null && !name.isBlank()) {

            specification = specification.and(
                    ProductSpecification.hasName(
                            name.trim()
                    )
            );
        }

        // Category filter
        if (categoryId != null) {

            specification = specification.and(
                    ProductSpecification.hasCategory(
                            categoryId
                    )
            );
        }

        // Minimum price
        if (minPrice != null) {

            specification = specification.and(
                    ProductSpecification
                            .priceGreaterThanOrEqual(
                                    minPrice
                            )
            );
        }

        // Maximum price
        if (maxPrice != null) {

            specification = specification.and(
                    ProductSpecification
                            .priceLessThanOrEqual(
                                    maxPrice
                            )
            );
        }

        // Active filter
        if (active != null && active) {

            specification = specification.and(
                    ProductSpecification.isActive()
            );
        }

        return productRepository
                .findAll(
                        specification,
                        pageable
                )
                .map(this::convertToResponse);
    }

    private ProductResponse convertToResponse(
            Product product) {

        ProductResponse response =
                new ProductResponse();

        response.setId(
                product.getId()
        );

        response.setName(
                product.getName()
        );

        response.setDescription(
                product.getDescription()
        );

        response.setPrice(
                product.getPrice()
        );

        if (product.getCategory() != null) {

            response.setCategoryId(
                    product.getCategory().getId()
            );

            response.setCategoryName(
                    product.getCategory().getName()
            );
        }

        // --------------------------------------------------------
        // Active
        // --------------------------------------------------------

        response.setActive(
                product.getActive()
        );

        List<ProductImageResponse> imageResponses =
                product.getImages()
                        .stream()
                        .map(image -> {

                            ProductImageResponse imageResponse =
                                    new ProductImageResponse();

                            imageResponse.setId(
                                    image.getId()
                            );

                            imageResponse.setImageUrl(
                                    image.getImageUrl()
                            );

                            imageResponse.setDisplayOrder(
                                    image.getDisplayOrder()
                            );

                            imageResponse.setPrimaryImage(
                                    image.getPrimaryImage()
                            );

                            return imageResponse;
                        })
                        .toList();

        response.setImages(
                imageResponses
        );

        List<ProductVariantResponse> variantResponses =
                product.getVariants()
                        .stream()
                        .map(variant -> {

                            ProductVariantResponse variantResponse =
                                    new ProductVariantResponse();

                            variantResponse.setId(
                                    variant.getId()
                            );

                            variantResponse.setSku(
                                    variant.getSku()
                            );

                            variantResponse.setSize(
                                    variant.getSize()
                            );

                            variantResponse.setColor(
                                    variant.getColor()
                            );

                            variantResponse.setAdditionalPrice(
                                    variant.getAdditionalPrice()
                            );

                            variantResponse.setActive(
                                    variant.getActive()
                            );

                            return variantResponse;
                        })
                        .toList();

        response.setVariants(
                variantResponses
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