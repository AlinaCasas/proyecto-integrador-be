package com.proyecto.integrador.reservation;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateReservationDTO {
    @Positive(message = "User id should be greater than 0")
    private Integer userId;

    @Positive(message = "Product id should be greater than 0")
    private Long productId;

    @Min(value = 10, message = "Start date of reservation should be a timestamp format")
    @Positive(message = "Start date of reservation is invalid")
    private Long startDate;

    @Min(value = 10, message = "End date of reservation should be a timestamp format")
    @Positive(message = "Start date of reservation is invalid")
    private Long endDate;

    @AssertTrue(message = "Reservation not confirmed")
    private boolean confirm;
}
