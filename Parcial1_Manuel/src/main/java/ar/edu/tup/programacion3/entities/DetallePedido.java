package ar.edu.tup.programacion3.entities;

public class DetallePedido extends Base {

    private Integer cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(Integer cantidad, Producto producto) {
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        this.subtotal = cantidad * producto.getPrecio();
    }

    public Integer getCantidad() { return cantidad; }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public Double getSubtotal() { return subtotal; }

    public Producto getProducto() { return producto; }

    public void setProducto(Producto producto) {
        this.producto = producto;
        calcularSubtotal();
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + getId() +
                ", producto='" + (producto != null ? producto.getNombre() : "N/A") + '\'' +
                ", cantidad=" + cantidad +
                ", subtotal=$" + subtotal +
                '}';
    }
}
