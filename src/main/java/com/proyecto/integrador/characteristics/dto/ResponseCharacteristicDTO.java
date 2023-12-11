package com.proyecto.integrador.characteristics.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ResponseCharacteristicDTO {
    private String name;
    private String image;
}