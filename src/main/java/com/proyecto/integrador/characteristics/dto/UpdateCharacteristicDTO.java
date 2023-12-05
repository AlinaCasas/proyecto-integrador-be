package com.proyecto.integrador.characteristics.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCharacteristicDTO {
    @NotNull(message = "Characteristic name is required")
    private String name;
}