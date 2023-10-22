package com.proyecto.integrador.modules.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String instrument;
    private String category;
    private String brand;
    private String model;
    private String description;
    private Float price;
    private List<String> images;
    private Integer discount;
}
