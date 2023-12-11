package com.proyecto.integrador.product.dto;

import com.proyecto.integrador.category.Category;
import com.proyecto.integrador.product.dto.ProductReservationDTO;
import com.proyecto.integrador.characteristics.dto.ResponseCharacteristicDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CreateProductDTO {
    @NotEmpty(message = "Instrument name is required, for example: Guitar")
    @Size(min = 3, max = 50, message = "Instrument name should be between 3 and 50 characters")
    private String name;

    private String categoryName;

    @Size(min = 3, max = 50, message = "Brand should be between 3 and 50 characters")
    private String brand;

    @Size(min = 3, max = 50, message = "Model should be between 3 and 50 characters")
    private String model;

    @Size(min = 3, max = 255, message = "Description should be between 3 and 255 characters")
    private String description;

    @Positive(message = "Price should be greater than 0")
    private Float price;

    @Size.List({
            @Size(max = 7, message = "Maximum 7 images are allowed")
    })
    private List<String> images;

    @Column(columnDefinition = "integer default 0")
    @Min(value = 0, message = "Discount should be greater than 0%")
    @Max(value = 100, message = "Discount should be less than 100%")
    private Integer discount;

    private List<String> characteristics;
}
