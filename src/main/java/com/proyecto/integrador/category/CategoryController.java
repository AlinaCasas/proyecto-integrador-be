package com.proyecto.integrador.category;
import com.proyecto.integrador.category.dto.CategoryDTO;
import com.proyecto.integrador.category.dto.UpdateCategoryDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable(value = "name", required = true) String name) {
        return categoryService.getCategoryByName(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{name}")
    public Category updateCategoryDescription(
            @PathVariable String name, @RequestBody @Valid UpdateCategoryDTO updateCategoryDTO
    ) {
        return categoryService.updateCategory(name, updateCategoryDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    public void softDeleteCategory(@PathVariable String name) {
        categoryService.softDeleteCategory(name);
    }
}