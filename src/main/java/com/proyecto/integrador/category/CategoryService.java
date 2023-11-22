package com.proyecto.integrador.category;

import com.proyecto.integrador.category.dto.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAllByDeletedAtIsNull();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
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
        throw new EntityNotFoundException("Category not found with name: " + name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteCategory(String name) {
        Category category = categoryRepository.findByName(name);
        if (category != null) {
            category.setDeletedAt(new Date());
            categoryRepository.save(category);
        } else {
            throw new EntityNotFoundException("Category not found with name: " + name);
        }
    }
}