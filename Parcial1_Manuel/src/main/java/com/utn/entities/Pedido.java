package com.utn.entities;

import com.utn.enums.Estado;
import com.utn.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Pedido extends Base implements Calculable {

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Double total;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    public void addDetallePedido(Integer cantidad, Producto producto) {
        DetallePedido detalle = DetallePedido.builder()
                .cantidad(cantidad)
                .producto(producto)
                .pedido(this)
                .build();
        detalle.calcularSubtotal();
        this.detalles.add(detalle);
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        return detalles.stream()
                .filter(d -> d.getProducto().equals(producto))
                .findFirst()
                .orElse(null);
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        detalles.removeIf(d -> d.getProducto().equals(producto));
        calcularTotal();
    }

    @Override
    public void calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            this.total = 0.0;
            return;
        }
        this.total = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }

}
