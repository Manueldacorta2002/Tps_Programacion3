package com.utn.entities;

import com.utn.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
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

    @Column(unique = true, nullable = false)
    private String mail;

    private String celular;

    @ToString.Exclude
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Pedido> pedidos = new ArrayList<>();

}
