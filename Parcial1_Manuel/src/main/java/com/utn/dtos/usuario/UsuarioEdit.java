package com.utn.dtos.usuario;

import com.utn.enums.Rol;

public record UsuarioEdit(
        String nombre,
        String apellido,
        String mail,
        String celular,
        String contrasenia,
        Rol rol
) {
}

