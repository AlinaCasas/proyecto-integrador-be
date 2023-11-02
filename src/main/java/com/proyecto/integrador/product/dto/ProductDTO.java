package com.proyecto.integrador.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private String brand;
    private String model;
    private String description;
    private Float price;
    private List<String> images;
    private Integer discount;
    private List<ProductReservationDTO> reservations;
}
