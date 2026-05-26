package com.utn.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, of = {"nombre"})
public class Producto extends Base {

    private String nombre;
    private Double precio;
    private String descripcion;
    private Integer stock;
    private String imagen;
    private Boolean disponible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    private Categoria categoria;

}
