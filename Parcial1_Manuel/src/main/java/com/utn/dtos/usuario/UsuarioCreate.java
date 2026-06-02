package com.utn.dtos.usuario;

import com.utn.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCreate(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        String apellido,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es valido")
        String mail,
        @NotBlank(message = "El celular es obligatorio")
        String celular,
        @NotBlank(message = "La contrasenia es obligatoria")
        @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
        String contrasenia,
        Rol rol
) {
}
