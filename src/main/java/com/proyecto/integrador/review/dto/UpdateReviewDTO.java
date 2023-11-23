package com.proyecto.integrador.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateReviewDTO {

    @Min(value = 0, message = "Rating should be greater than 0")
    @Max(value = 5, message = "Rating should be less than 5")
    private Float rating;

    @Min(value = 3, message = "Comment should be greater than 3 characters")
    @Max(value = 255, message = "Comment should be less than 255 characters")
    private String comment;
}
