package com.proyecto.integrador.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReservationDTO {

//    @JsonIgnore
//    @Schema(hidden = true)
//    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
//    @Hidden
    @Positive(message = "User id should be greater than 0")
    private Integer userId;

    @NotNull(message = "Product id is required")
    @Positive(message = "Product id should be greater than 0")
    private Long productId;

    @NotNull(message = "Start date of reservation is required")
    @Min(value = 10, message = "Start date of reservation should be a timestamp format")
    @Positive(message = "Start date of reservation is invalid")
    private Long startDate;

    @NotNull(message = "End date of reservation is required")
    @Min(value = 10, message = "End date of reservation should be a timestamp format")
    @Positive(message = "Start date of reservation is invalid")
    private Long endDate;
}
