package com.utn.dtos.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoCreate(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        Double precio,
        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion,
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
        Integer stock,
        String imagen,
        @NotNull(message = "La disponibilidad es obligatoria")
        Boolean disponible,
        @NotNull(message = "La categoria es obligatoria")
        Long categoriaId
) {
}
