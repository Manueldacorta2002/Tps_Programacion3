package com.utn.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es valido")
        String mail,
        @NotBlank(message = "La contrasenia es obligatoria")
        @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
        String contrasenia
) {
}
