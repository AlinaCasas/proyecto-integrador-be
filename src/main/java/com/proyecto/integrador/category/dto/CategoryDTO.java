package com.proyecto.integrador.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotNull(message = "Category name is required")
    @Size(min = 3, max = 50, message = "Category name should be between 3 and 50 characters")
    private String name;

    @Size(min = 3, max = 255, message = "Description should be between 3 and 255 characters")
    private String description;

}