package com.proyecto.integrador.product.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductReservationDTO {
    private Long id;
    private Long startDate;
    private Long endDate;
}
