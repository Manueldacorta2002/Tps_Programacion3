package com.utn.dtos;

import com.utn.entities.Usuario;

// Record que expone solo los datos públicos del usuario
// No incluye contrasenia ni rol (datos sensibles)
public record UsuarioDTO(
        Long id,
        String nombre,
        String apellido,
        String mail,
        String celular
) {

    // Método de fábrica para crear el DTO desde una entidad Usuario
    public static UsuarioDTO fromUsuario(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getMail(),
                usuario.getCelular()
        );
    }

}
