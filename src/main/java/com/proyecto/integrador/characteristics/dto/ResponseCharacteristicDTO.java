package com.proyecto.integrador.characteristics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ResponseCharacteristicDTO {
    private String name;
    private String image;
}