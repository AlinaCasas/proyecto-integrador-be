package com.proyecto.integrador.category;

import com.proyecto.integrador.category.dto.CategoryDTO;
import com.proyecto.integrador.category.dto.UpdateCategoryDTO;
import com.proyecto.integrador.exceptions.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findByName(categoryDTO.getName()).orElse(null);
        if (existingCategory != null) {
            throw new BadRequestException("Category already exists with name: " + categoryDTO.getName());
        }
        String name = categoryDTO.getName();
        String description = categoryDTO.getDescription();
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Unexpected error, please contact support.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(String name, UpdateCategoryDTO updateCategoryDTO) {
        Category existingCategory = categoryRepository.findByName(name).orElseThrow(() -> new BadRequestException("Category not found with name: " + name));
        existingCategory.setDescription(updateCategoryDTO.getDescription());
        return categoryRepository.save(existingCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteCategory(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(() -> new BadRequestException("Category not found with name: " + name));
        category.setDeletedAt(new Date());
        categoryRepository.save(category);
    }
}