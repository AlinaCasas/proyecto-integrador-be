package com.proyecto.integrador.characteristics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CharacteristicDTO {
    @NotNull(message = "Characteristic name is required")
    private String name;
}
