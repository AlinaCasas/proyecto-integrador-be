package com.proyecto.integrador.reservation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateReservationDTO {
    @Positive(message = "User id should be greater than 0")
    private Integer userId;

    @NotNull(message = "Product id is required")
    @Positive(message = "Product id should be greater than 0")
    private Long productId;

    @NotNull(message = "Start date of reservation is required")
    @Min(value = 1672531200, message = "Start date of reservation should be a timestamp format")
    @Positive(message = "Start date of reservation is invalid")
    private Long startDate;

    @NotNull(message = "End date of reservation is required")
    @Min(value = 1672531200, message = "End date of reservation should be a timestamp format")
    @Positive(message = "Start date of reservation is invalid")
    private Long endDate;
}
