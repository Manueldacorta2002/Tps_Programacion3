package ar.edu.tup.programacion3;

import ar.edu.tup.programacion3.entities.*;
import ar.edu.tup.programacion3.enums.Estado;
import ar.edu.tup.programacion3.enums.FormaPago;
import ar.edu.tup.programacion3.enums.Rol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // ===================== CATEGORÍAS =====================
        Categoria hamburguesas = new Categoria("Hamburguesas", "Hamburguesas artesanales a la parrilla");
        Categoria pizzas       = new Categoria("Pizzas", "Pizzas a la piedra con ingredientes frescos");
        Categoria bebidas      = new Categoria("Bebidas", "Bebidas frías y aguas");

        // ===================== PRODUCTOS =====================
        // Algunos con disponible=false y algunos con stock < 5
        Producto p1  = new Producto("Hamburguesa Clásica",  1500.0, "Carne, lechuga, tomate y queso",       10, "burger.png",        true,  hamburguesas);
        Producto p2  = new Producto("Hamburguesa Doble",    2200.0, "Doble medallón con queso cheddar",      3, "double-burger.png", true,  hamburguesas); // stock < 5
        Producto p3  = new Producto("Hamburguesa Veggie",   1800.0, "Con medallón de garbanzos",             7, "veggie-burger.png", false, hamburguesas); // no disponible
        Producto p4  = new Producto("Pizza Muzzarella",     2800.0, "Salsa de tomate y mozzarella",         12, "pizza.png",         true,  pizzas);
        Producto p5  = new Producto("Pizza Pepperoni",      3200.0, "Con pepperoni y queso",                 2, "pizza-pep.png",     true,  pizzas);        // stock < 5
        Producto p6  = new Producto("Pizza Napolitana",     3000.0, "Con tomate, albahaca y ajo",            8, "pizza-napo.png",    true,  pizzas);
        Producto p7  = new Producto("Papas Fritas",         1200.0, "Papas crujientes con ketchup",         20, "fries.png",         true,  bebidas);
        Producto p8  = new Producto("Gaseosa",               800.0, "Gaseosa 500ml",                        50, "drink.png",         true,  bebidas);
        Producto p9  = new Producto("Agua Mineral",          400.0, "Agua mineral 500ml",                    4, "water.png",         true,  bebidas);       // stock < 5
        Producto p10 = new Producto("Combo Familiar",       5500.0, "2 hamburguesas + papas + 2 bebidas",    0, "combo.png",         false, hamburguesas);  // stock < 5 y no disponible

        List<Producto> productos = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10));

        // ===================== USUARIOS =====================
        Usuario admin   = new Usuario("Manuel",  "García",   "admin@tup.com",   "2615000001", "admin123", Rol.ADMIN);
        Usuario cliente = new Usuario("Laura",   "Martínez", "laura@mail.com",  "2615000002", "pass456",  Rol.USUARIO);

        // ===================== PEDIDOS =====================

        // Pedido 1 — Manuel (admin)
        Pedido pedido1 = new Pedido(LocalDate.of(2025, 4, 10), Estado.CONFIRMADO, FormaPago.TARJETA, admin);
        pedido1.agregarDetalle(2, p1);   // 2 x Hamburguesa Clásica   = $3000
        pedido1.agregarDetalle(2, p8);   // 2 x Gaseosa               = $1600
        pedido1.agregarDetalle(1, p7);   // 1 x Papas Fritas           = $1200

        // Pedido 2 — Laura (cliente)
        Pedido pedido2 = new Pedido(LocalDate.of(2025, 4, 15), Estado.TERMINADO, FormaPago.EFECTIVO, cliente);
        pedido2.agregarDetalle(1, p4);   // 1 x Pizza Muzzarella      = $2800
        pedido2.agregarDetalle(2, p9);   // 2 x Agua Mineral           = $800

        // Pedido 3 — Laura (cliente)
        Pedido pedido3 = new Pedido(LocalDate.of(2025, 4, 20), Estado.PENDIENTE, FormaPago.TRANSFERENCIA, cliente);
        pedido3.agregarDetalle(1, p2);   // 1 x Hamburguesa Doble     = $2200
        pedido3.agregarDetalle(1, p5);   // 1 x Pizza Pepperoni        = $3200
        pedido3.agregarDetalle(3, p8);   // 3 x Gaseosa               = $2400

        // =====================================================================
        // CONSIGNA 1 — PRODUCTOS DISPONIBLES (Stream + filter)
        // =====================================================================
        System.out.println("--- PRODUCTOS DISPONIBLES ---");
        productos.stream()
                .filter(Producto::getDisponible)
                .forEach(System.out::println);

        // =====================================================================
        // CONSIGNA 2 — TOTAL DEL PEDIDO (calcularTotal con Stream en Pedido)
        // =====================================================================
        System.out.println("\n--- TOTAL DEL PEDIDO ---");
        System.out.println("Pedido 1 (" + pedido1.getUsuario().getNombre() + "): $" + pedido1.getTotal());
        System.out.println("Pedido 2 (" + pedido2.getUsuario().getNombre() + "): $" + pedido2.getTotal());
        System.out.println("Pedido 3 (" + pedido3.getUsuario().getNombre() + "): $" + pedido3.getTotal());

        // =====================================================================
        // CONSIGNA 3 — CANTIDAD TOTAL DE ÍTEMS DEL PEDIDO (Stream + mapToInt + sum)
        // =====================================================================
        System.out.println("\n--- CANTIDAD TOTAL DE ÍTEMS DEL PEDIDO ---");
        int cantidadItemsPedido1 = pedido1.getDetalles().stream()
                .mapToInt(DetallePedido::getCantidad)
                .sum();
        System.out.println("El pedido 1 tiene " + cantidadItemsPedido1 + " ítems");

        int cantidadItemsPedido2 = pedido2.getDetalles().stream()
                .mapToInt(DetallePedido::getCantidad)
                .sum();
        System.out.println("El pedido 2 tiene " + cantidadItemsPedido2 + " ítems");

        int cantidadItemsPedido3 = pedido3.getDetalles().stream()
                .mapToInt(DetallePedido::getCantidad)
                .sum();
        System.out.println("El pedido 3 tiene " + cantidadItemsPedido3 + " ítems");

        // =====================================================================
        // CONSIGNA 4 — PRODUCTOS CON STOCK MENOR A 5 (Stream + filter)
        // =====================================================================
        System.out.println("\n--- PRODUCTOS CON STOCK MENOR A 5 ---");
        productos.stream()
                .filter(producto -> producto.getStock() < 5)
                .forEach(System.out::println);
    }
}
