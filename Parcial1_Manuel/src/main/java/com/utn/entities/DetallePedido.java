package com.utn.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DetallePedido {

    private Integer cantidad;
    private Double subtotal;
    private Producto producto;

    // Calcula el subtotal en base a la cantidad y el precio del producto
    public void calcularSubtotal() {
        if (producto != null) {
            this.subtotal = cantidad * producto.getPrecio();
        }
    }

}
