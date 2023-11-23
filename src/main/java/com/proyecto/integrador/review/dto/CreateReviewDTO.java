package com.proyecto.integrador.review.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReviewDTO {
    @NotNull(message = "Product id is required")
    @Positive(message = "Product id should be greater than 0")
    private Long productId;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating should be greater than 0")
    @Max(value = 5, message = "Rating should be less than 5")
    private Float rating;

    @NotNull(message = "Comment is required")
    @Size(min = 3, max = 255, message = "Comment should be between 3 and 255 characters")
    private String comment;
}
