package com.proyecto.integrador.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
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
}
