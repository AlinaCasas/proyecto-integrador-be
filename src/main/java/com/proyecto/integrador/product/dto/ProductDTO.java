package com.proyecto.integrador.product.dto;

import com.proyecto.integrador.product.dto.ProductReservationDTO;
import com.proyecto.integrador.characteristics.dto.ResponseCharacteristicDTO;
import jakarta.persistence.Column;
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
    private Float rating;
    private Integer ratingCount;
    private List<String> images;
    private Integer discount;
    private List<ProductReservationDTO> reservations;
    private List<ResponseCharacteristicDTO> characteristics;
    private List<String> policies;
}
