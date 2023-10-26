package com.proyecto.integrador.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateProductDTO {

    @NotEmpty(message = "Instrument name is required, foe example: Guitar")
    @Size(min = 3, max = 50, message = "Instrument name should be between 3 and 50 characters")
    private String name;

    @NotEmpty(message = "Category is required")
    private String category;

    @NotEmpty(message = "Brand is required, for example: Casio")
    @Size(min = 3, max = 50, message = "Brand should be between 3 and 50 characters")
    private String brand;

    @Size(min = 3, max = 50, message = "Model should be between 3 and 50 characters")
    private String model;

    @Size(min = 3, max = 255, message = "Description should be between 3 and 255 characters")
    private String description;

    @Positive(message = "Price should be greater than 0")
    private Float price;

    private List<String> images;

    @Min(value = 0, message = "Discount should be greater than 0%")
    @Max(value = 100, message = "Discount should be less than 100%")
    private Integer discount;
}
