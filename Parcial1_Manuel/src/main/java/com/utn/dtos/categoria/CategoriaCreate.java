package com.utn.dtos.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaCreate(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion
) {
}
