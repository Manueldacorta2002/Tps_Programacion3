package com.tup.programacion3.entities;

import java.util.Objects;

public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        this.subtotal = cantidad * producto.getPrecio();
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; calcularSubtotal(); }

    public Double getSubtotal() { return subtotal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; calcularSubtotal(); }

    // Evita recursión: muestra solo el nombre del producto
    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + getId() +
                ", producto='" + (producto != null ? producto.getNombre() : "N/A") + '\'' +
                ", cantidad=" + cantidad +
                ", subtotal=$" + subtotal +
                '}';
    }

    // Identidad basada en: producto (no puede haber dos detalles del mismo producto en un pedido)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallePedido that = (DetallePedido) o;
        return Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto);
    }
}
