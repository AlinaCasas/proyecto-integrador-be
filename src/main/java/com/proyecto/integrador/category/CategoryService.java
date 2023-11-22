package com.proyecto.integrador.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByDeletedAtIsNull();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(Category category) {
        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());
        return categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(String name, String description) {
        Category existingCategory = categoryRepository.findByName(name);
        if (existingCategory != null) {
            existingCategory.setDescription(description);
            existingCategory.setUpdatedAt(new Date());
            return categoryRepository.save(existingCategory);
        }
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteCategory(String name) {
        Category category = categoryRepository.findByName(name);
        if (category != null) {
            category.setDeletedAt(new Date());
            categoryRepository.save(category);
        }
    }
}