package com.proyecto.integrador.category;
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
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{name}")
    public Category updateCategoryDescription(
            @PathVariable String name,
            @RequestParam String description
    ) {
        // Se debería implementar la lógica de autorización aquí
        // Por ejemplo, verificar si el usuario actual tiene roles de administrador
        // y si no, lanzar una excepción de acceso no autorizado
        return categoryService.updateCategory(name, description);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    public void softDeleteCategory(@PathVariable String name) {
        // Se debería implementar la lógica de autorización aquí
        // Por ejemplo, verificar si el usuario actual tiene roles de administrador
        // y si no, lanzar una excepción de acceso no autorizado
        categoryService.softDeleteCategory(name);
    }
}