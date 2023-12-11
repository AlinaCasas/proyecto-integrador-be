package com.proyecto.integrador.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchProductDTO {
    private List<String> suggestionsNames;

    private List<ProductDTO> suggestionsProducts;
}
