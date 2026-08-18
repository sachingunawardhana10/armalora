package com.armalora.product.service;

import com.armalora.product.entity.Category;
import com.armalora.product.exception.CategoryNotFoundException;
import com.armalora.product.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {

        if (categoryRepository
                .findByNameIgnoreCase(category.getName())
                .isPresent()) {

            throw new RuntimeException(
                    "Category already exists: "
                            + category.getName()
            );
        }

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {

        return categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(id) );
    }
}