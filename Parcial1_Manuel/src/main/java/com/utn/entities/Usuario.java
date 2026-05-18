package com.utn.entities;

import com.utn.enums.Rol;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private String mail;
    private String celular;

    // La contraseña no se muestra en toString
    @ToString.Exclude
    private String contrasenia;

    private Rol rol;

}
